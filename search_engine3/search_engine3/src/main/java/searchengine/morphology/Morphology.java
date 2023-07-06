// Declare a package named "searchengine.morphology"
package searchengine.morphology;

// Import necessary classes
import java.util.HashMap;
import java.util.List;

// Declare an interface named "Morphology"
public interface Morphology {

    // Define a method called 'getLemmaList'. This method takes a string 'content' as input
    // and returns a HashMap with keys as string and values as integers.
    // This method is expected to analyze the 'content' and return a list of lemmas (base or dictionary form of words),
    // along with their counts in the content.
    HashMap<String, Integer> getLemmaList(String content);

    // Define a method called 'getLemma'. This method takes a string 'word' as input
    // and returns a List of strings. This method is expected to analyze the 'word'
    // and return its possible lemmas.
    List<String> getLemma(String word);

    // Define a method called 'findLemmaIndexInText'. This method takes a string 'content'
    // and a string 'lemma' as inputs and returns a list of integers.
    // This method is expected to find the positions of the specified lemma in the 'content'.
    List<Integer> findLemmaIndexInText(String content, String lemma);
}
