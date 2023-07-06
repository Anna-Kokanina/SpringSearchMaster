package searchengine.dto.statistics;

import lombok.Value;

@Value
public class LemmaStatistics {
    String lemma;       // Lemma string
    int frequency;      // Frequency of the lemma
}
