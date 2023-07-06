package searchengine.morphology;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
// This class is responsible for analyzing and processing text data
public class Analyzer implements Morphology {
    private static RussianLuceneMorphology russianLuceneMorphology;
    // Regular expression to match unwanted characters
    private final static String REGEX = "\\p{Punct}|[0-9]|№|©|◄|«|»|—|-|@|…";
    // Marker for invalid symbols
    private final static Marker INVALID_SYMBOL_MARKER = MarkerManager.getMarker("INVALID_SYMBOL");
    // Logger for this class
    private final static Logger LOGGER = LogManager.getLogger(LuceneMorphology.class);

    // Static block to initialize RussianLuceneMorphology
    static {
        try {
            russianLuceneMorphology = new RussianLuceneMorphology();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    // Method to get a list of lemmas from a given content
    @Override
    public HashMap<String, Integer> getLemmaList(String content) {
        // Convert content to lower case and remove unwanted characters
        content = content.toLowerCase(Locale.ROOT)
                .replaceAll(REGEX, " ");
        HashMap<String, Integer> lemmaList = new HashMap<>();
        // Split content into words
        String[] elements = content.toLowerCase(Locale.ROOT).split("\\s+");
        for (String el : elements) {
            // Get lemmas for each word
            List<String> wordsList = getLemma(el);
            for (String word : wordsList) {
                // Count occurrences of each lemma
                int count = lemmaList.getOrDefault(word, 0);
                lemmaList.put(word, count + 1);
            }
        }
        return lemmaList;
    }

    // Method to get lemmas for a given word
    @Override
    public List<String> getLemma(String word) {
        List<String> lemmaList = new ArrayList<>();
        try {
            // Get base form of the word
            List<String> baseRusForm = russianLuceneMorphology.getNormalForms(word);
            // If the word is not a service word, add it to the list
            if (!isServiceWord(word)) {
                lemmaList.addAll(baseRusForm);
            }
        } catch (Exception e) {
            // Log if the symbol is not found
            LOGGER.debug(INVALID_SYMBOL_MARKER, "Символ не найден - " + word);
        }
        return lemmaList;
    }

    // Method to find the index of a given lemma in the content
    @Override
    public List<Integer> findLemmaIndexInText(String content, String lemma) {
        List<Integer> lemmaIndexList = new ArrayList<>();
        // Split content into words
        String[] elements = content.toLowerCase(Locale.ROOT).split("\\p{Punct}|\\s");
        int index = 0;
        for (String el : elements) {
            // Get lemmas for each word
            List<String> lemmas = getLemma(el);
            for (String lem : lemmas) {
                //// If the lemma matches the given lemma, add its index to the list
                if (lem.equals(lemma)) {
                    lemmaIndexList.add(index);
                }
            }
            // Update the index for the next word
            index += el.length() + 1;
        }
        return lemmaIndexList;
    }

    // Method to check if a word is a service word
    private boolean isServiceWord(String word) {
        // Get morphological information of the word
        List<String> morphForm = russianLuceneMorphology.getMorphInfo(word);
        for (String l : morphForm) {
            // Check if the word is a preposition, conjunction, particle, or if it is too short
            if (l.contains("ПРЕДЛ")
                    || l.contains("СОЮЗ")
                    || l.contains("МЕЖД")
                    || l.contains("МС")
                    || l.contains("ЧАСТ")
                    || l.length() <= 3) {
                return true;
            }
        }
        return false;
    }
}
