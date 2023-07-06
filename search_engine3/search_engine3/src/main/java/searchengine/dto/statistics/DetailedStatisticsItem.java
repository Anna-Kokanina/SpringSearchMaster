package searchengine.dto.statistics;

import lombok.Value;
import searchengine.model.Status;

import java.util.Date;

@Value
public class DetailedStatisticsItem {
    String url;         // URL associated with the statistics item
    String name;        // Name of the statistics item
    Status status;      // Status of the item (from the Status enum)
    Date statusTime;    // Time of the status
    String error;       // Error message associated with the item (if any)
    long pages;         // Number of pages related to the item
    long lemmas;        // Number of lemmas related to the item
}
