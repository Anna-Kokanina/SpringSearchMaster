package searchengine.parsers;

// importing necessary libraries and modules
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.IndexStatistics;
import searchengine.dto.statistics.LemmaStatistics;
import searchengine.dto.statistics.PageStatistics;
import searchengine.model.*;
import searchengine.repository.IndexSearchRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;

// using Lombok to simplify code by auto-generating constructors, getters, setters, etc.
@RequiredArgsConstructor
// using Slf4j to simplify logging
@Slf4j
public class SiteIndexed implements Runnable {

    // defining the necessary repositories and parsers
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final LemmaRepository lemmaRepository;
    // defining the number of available processors for ForkJoinPool
    private static final int coreAmount = Runtime.getRuntime().availableProcessors();
    private final IndexSearchRepository indexSearchRepository;
    private final LemmaParser lemmaParser;
    private final IndexParser indexParser;
    // url of the site to be indexed
    private final String url;
    private final SitesList sitesList;

    // method to fetch a list of pages from a site
    private List<PageStatistics> getPageDtoList() throws InterruptedException {
        if (!Thread.interrupted()) {
            String urlFormat = url + "/";
            List<PageStatistics> pageStatisticsVector = new Vector<>();
            List<String> urlList = new Vector<>();
            // ForkJoinPool for parallel execution
            ForkJoinPool forkJoinPool = new ForkJoinPool(coreAmount);
            List<PageStatistics> pages = forkJoinPool.invoke(new UrlParser(urlFormat, pageStatisticsVector, urlList));
            return new CopyOnWriteArrayList<>(pages);
        } else throw new InterruptedException();
    }

    // Runnable's run method that contains the main logic for site indexing
    @Override
    public void run() {
        // delete previous data from the site
        deleteDataFromSite();
        log.info("Indexing - " + url + " " + getName());
        SitePage sitePage = saveDateSite();
        try {
            List<PageStatistics> pageStatisticsList = getPageDtoList();
            saveToBase(pageStatisticsList);
            getLemmasPage();
            indexingWords();
        } catch (InterruptedException e) {
            log.error("Indexing stopped - " + url);
            errorSite(sitePage);
        }
    }

    // method to get and save lemmas from a page
    private void getLemmasPage() {
        if (!Thread.interrupted()) {
            SitePage sitePage = siteRepository.findByUrl(url);
            sitePage.setStatusTime(new Date());
            lemmaParser.run(sitePage);
            List<LemmaStatistics> lemmaStatisticsList = lemmaParser.getLemmaDtoList();
            List<Lemma> lemmaList = new CopyOnWriteArrayList<>();
            for (LemmaStatistics lemmaStatistics : lemmaStatisticsList) {
                lemmaList.add(new Lemma(lemmaStatistics.getLemma(), lemmaStatistics.getFrequency(), sitePage));
            }
            lemmaRepository.flush();
            lemmaRepository.saveAll(lemmaList);
        } else {
            throw new RuntimeException();
        }
    }

    // method to save a list of pages to the database
    private void saveToBase(List<PageStatistics> pages) throws InterruptedException {
        if (!Thread.interrupted()) {
            List<Page> pageList = new CopyOnWriteArrayList<>();
            SitePage site = siteRepository.findByUrl(url);
            for (PageStatistics page : pages) {
                int first = page.getUrl().indexOf(url) + url.length();
                String format = page.getUrl().substring(first);
                pageList.add(new Page(site, format, page.getCode(),
                        page.getContent()));
            }
            pageRepository.flush();
            pageRepository.saveAll(pageList);
        } else {
            throw new InterruptedException();
        }
    }

    // method to delete previous data from a site
    private void deleteDataFromSite() {
        SitePage sitePage = siteRepository.findByUrl(url);
        if (sitePage != null) {
            log.info("Start delete site data - " + url);
            sitePage.setStatus(Status.INDEXING);
            sitePage.setName(getName());
            sitePage.setStatusTime(new Date());
            siteRepository.save(sitePage);
            siteRepository.flush();
            siteRepository.delete(sitePage);
        }
    }

    // method to index words on a page
    private void indexingWords() throws InterruptedException {
        if (!Thread.interrupted()) {
            SitePage sitePage = siteRepository.findByUrl(url);
            indexParser.run(sitePage);
            List<IndexStatistics> indexStatisticsList = new CopyOnWriteArrayList<>(indexParser.getIndexList());
            List<SearchIndex> indexList = new CopyOnWriteArrayList<>();
            sitePage.setStatusTime(new Date());
            for (IndexStatistics indexStatistics : indexStatisticsList) {
                Page page = pageRepository.getById(indexStatistics.getPageID());
                Lemma lemma = lemmaRepository.getById(indexStatistics.getLemmaID());
                indexList.add(new SearchIndex(page, lemma, indexStatistics.getRank()));
            }
            indexSearchRepository.flush();
            indexSearchRepository.saveAll(indexList);
            log.info("Done indexing - " + url);
            sitePage.setStatusTime(new Date());
            sitePage.setStatus(Status.INDEXED);
            siteRepository.save(sitePage);
        } else {
            throw new InterruptedException();
        }
    }

    // method to save site details
    private SitePage saveDateSite() {
        SitePage sitePage = new SitePage();
        sitePage.setUrl(url);
        sitePage.setName(getName());
        sitePage.setStatus(Status.INDEXING);
        sitePage.setStatusTime(new Date());
        siteRepository.flush();
        return siteRepository.save(sitePage);
    }

    // method to get the name of a site
    private String getName() {
        List<Site> sitesList_2 = sitesList.getSites();
        for (Site map : sitesList_2) {
            if (map.getUrl().equals(url)) {
                return map.getName();
            }
        }
        return "";
    }

    // method to handle site errors
    private void errorSite(SitePage sitePage) {
        sitePage.setLastError("Indexing stopped");
        sitePage.setStatus(Status.FAILED);
        sitePage.setStatusTime(new Date());
        siteRepository.flush();
        siteRepository.save(sitePage);
    }
}
