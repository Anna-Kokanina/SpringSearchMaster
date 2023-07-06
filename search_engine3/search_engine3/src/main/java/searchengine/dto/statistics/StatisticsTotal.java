package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
public class StatisticsTotal {
     Long sites;         // Total number of sites
     Long pages;         // Total number of pages
     Long lemmas;        // Total number of lemmas
     boolean indexing;   // Indicates if indexing is in progress or not
}
