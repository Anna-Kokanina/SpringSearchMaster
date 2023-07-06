package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
public class StatisticsResponse {
    private boolean result;               // Indicates the result of the statistics response
    private StatisticsData statistics;    // Statistics data
}
