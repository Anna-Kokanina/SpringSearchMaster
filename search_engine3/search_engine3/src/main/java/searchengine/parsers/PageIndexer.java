package searchengine.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.IndexStatistics;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.SitePage;
import searchengine.morphology.Morphology;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.utils.HtmlCleaner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PageIndexer implements IndexParser {
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final Morphology morphology;
    private List<IndexStatistics> indexStatisticsList;

    @Override
    public List<IndexStatistics> getIndexList() {
        return indexStatisticsList;
    }

    @Override
    public void run(SitePage site) {
        // Retrieve pages and lemmas for the given site
        Iterable<Page> pageList = pageRepository.findBySiteId(site);
        List<Lemma> lemmaList = lemmaRepository.findBySitePageId(site);

        // Initialize the list to store index statistics
        indexStatisticsList = new ArrayList<>();

        // Process each page
        for (Page page : pageList) {
            // Check if the page has a valid status code
            if (page.getCode() < 400) {
                long pageId = page.getId();
                String content = page.getContent();

                // Extract the title and body content from the page
                String title = HtmlCleaner.clear(content, "title");
                String body = HtmlCleaner.clear(content, "body");

                // Get lemma lists for the title and body
                HashMap<String, Integer> titleList = morphology.getLemmaList(title);
                HashMap<String, Integer> bodyList = morphology.getLemmaList(body);

                // Process each lemma
                for (Lemma lemma : lemmaList) {
                    Long lemmaId = lemma.getId();
                    String theExactLemma = lemma.getLemma();

                    // Check if the lemma exists in the title or body lists
                    if (titleList.containsKey(theExactLemma) || bodyList.containsKey(theExactLemma)) {
                        float wholeRank = 0.0F;

                        // Calculate the rank based on lemma occurrence in the title and body
                        if (titleList.get(theExactLemma) != null) {
                            Float titleRank = Float.valueOf(titleList.get(theExactLemma));
                            wholeRank += titleRank;
                        }
                        if (bodyList.get(theExactLemma) != null) {
                            float bodyRank = (float) (bodyList.get(theExactLemma) * 0.8);
                            wholeRank += bodyRank;
                        }

                        // Add the index statistics to the list
                        indexStatisticsList.add(new IndexStatistics(pageId, lemmaId, wholeRank));
                    } else {
                        log.debug("Lemma not found");
                    }
                }
            } else {
                log.debug("Bad status code - " + page.getCode());
            }
        }
    }
}
