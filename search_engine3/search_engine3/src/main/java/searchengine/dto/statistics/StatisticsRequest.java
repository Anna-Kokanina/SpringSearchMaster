package searchengine.dto.statistics;

import lombok.Value;

@Value
public class StatisticsRequest {
    boolean gotResult;  // Indicates if the request operation resulted in a result or not
    String error;       // Error message associated with the request (if any)
}
