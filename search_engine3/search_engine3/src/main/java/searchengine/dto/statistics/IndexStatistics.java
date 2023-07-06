package searchengine.dto.statistics;

import lombok.Value;

@Value
public class IndexStatistics {
    Long pageID;     // ID of the page
    Long lemmaID;    // ID of the lemma
    Float rank;      // Rank of the index
}
