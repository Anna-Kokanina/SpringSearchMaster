package searchengine.parsers;

// Importing necessary libraries and modules
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.dto.statistics.PageStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

// Using Slf4j to simplify logging
@Slf4j
public class UrlParser extends RecursiveTask<List<PageStatistics>> {
    // Defining class variables for address and lists to store statistics and addresses
    private final String address;
    private final List<String> addressList;
    private final List<PageStatistics> pageStatisticsList;

    // Constructor to initialize class variables
    public UrlParser(String address, List<PageStatistics> pageStatisticsList, List<String> addressList) {
        this.address = address;
        this.pageStatisticsList = pageStatisticsList;
        this.addressList = addressList;
    }

    // Method to establish connection with the URL and return the document
    public Document getConnect(String url) {
        Document document = null;
        try {
            Thread.sleep(150);
            document = Jsoup.connect(url).userAgent(UserAgent.getUserAgent()).referrer("http://www.google.com").get();
        } catch (Exception e) {
            log.debug("Can't get connected to the site" + url);
        }
        return document;
    }

    // Overriding compute method of RecursiveTask for parallel processing
    @Override
    protected List<PageStatistics> compute() {
        try {
            Thread.sleep(150);
            Document document = getConnect(address);
            String html = document.outerHtml();
            Connection.Response response = document.connection().response();
            int statusCode = response.statusCode();
            PageStatistics pageStatistics = new PageStatistics(address, html, statusCode);
            pageStatisticsList.add(pageStatistics);
            Elements elements = document.select("body").select("a");
            List<UrlParser> taskList = new ArrayList<>();
            // For each link element, create a new UrlParser task and add it to taskList
            for (Element el : elements) {
                String link = el.attr("abs:href");
                if (link.startsWith(el.baseUri()) && !link.equals(el.baseUri()) && !link.contains("#") && !link.contains(".pdf") && !link.contains(".jpg") && !link.contains(".JPG") && !link.contains(".png") && !addressList.contains(link)) {
                    addressList.add(link);
                    UrlParser task = new UrlParser(link, pageStatisticsList, addressList);
                    task.fork(); // asynchronously execute the task
                    taskList.add(task);
                }
            }
            taskList.forEach(ForkJoinTask::join); // wait for all tasks to complete
        } catch (Exception e) {
            log.debug("Parsing error - " + address);
            PageStatistics pageStatistics = new PageStatistics(address, "", 500);
            pageStatisticsList.add(pageStatistics);
        }
        return pageStatisticsList;
    }
}
