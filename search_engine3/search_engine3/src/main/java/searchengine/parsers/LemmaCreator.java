// Declare a package named "searchengine.parsers"
package searchengine.parsers;

// Import necessary classes
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.LemmaStatistics;
import searchengine.model.Page;
import searchengine.model.SitePage;
import searchengine.morphology.Morphology;
import searchengine.repository.PageRepository;
import searchengine.utils.HtmlCleaner;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

// Annotate the class to be a Spring Component
@Component
// Annotate for RequiredArgsConstructor, which automatically generates a constructor
// for all final fields, and for fields marked with @NonNull.
@RequiredArgsConstructor
// Annotate for SLF4J, which gives a Logger instance that we can use for logging
@Slf4j
// Declare a class "LemmaCreator" that implements the "LemmaParser" interface
public class LemmaCreator implements LemmaParser {
    // Declare fields for PageRepository and Morphology
    private final PageRepository pageRepository;
    private final Morphology morphology;

    // Declare a list to hold LemmaStatistics objects
    private List<LemmaStatistics> lemmaStatisticsList;

    // Method to get the list of LemmaStatistics objects
    public List<LemmaStatistics> getLemmaDtoList() {
        return lemmaStatisticsList;
    }

    // Override the 'run' method from the LemmaParser interface
    @Override
    public void run(SitePage site) {
        // Initialize the lemmaStatisticsList as a thread-safe list
        lemmaStatisticsList = new CopyOnWriteArrayList<>();

        // Get all pages from the repository
        Iterable<Page> pageList = pageRepository.findAll();

        // Declare a TreeMap to store the lemma list
        TreeMap<String, Integer> lemmaList = new TreeMap<>();

        // Loop over each page
        for (Page page : pageList) {
            // Get the content of the page
            String content = page.getContent();

            // Clean up the title and body of the content
            String title = HtmlCleaner.clear(content, "title");
            String body = HtmlCleaner.clear(content, "body");

            // Get lemma lists of the title and the body
            HashMap<String, Integer> titleList = morphology.getLemmaList(title);
            HashMap<String, Integer> bodyList = morphology.getLemmaList(body);

            // Merge keys of the titleList and bodyList into a HashSet
            Set<String> allTheWords = new HashSet<>();
            allTheWords.addAll(titleList.keySet());
            allTheWords.addAll(bodyList.keySet());

            // Count the frequency of each word in the set
            for (String word : allTheWords) {
                int frequency = lemmaList.getOrDefault(word, 0) + 1;
                lemmaList.put(word, frequency);
            }
        }

        // Loop over each lemma in the lemma list
        for (String lemma : lemmaList.keySet()) {
            // Get the frequency of the lemma
            Integer frequency = lemmaList.get(lemma);

            // Add a new LemmaStatistics object to the lemmaStatisticsList
            lemmaStatisticsList.add(new LemmaStatistics(lemma, frequency));
        }
    }
}
