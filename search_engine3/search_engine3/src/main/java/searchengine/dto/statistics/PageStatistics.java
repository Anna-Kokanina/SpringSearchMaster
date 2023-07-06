package searchengine.dto.statistics;

import lombok.Value;

@Value
public class PageStatistics {
    String url;       // URL of the page
    String content;   // Content of the page
    int code;         // HTTP status code of the page
}
