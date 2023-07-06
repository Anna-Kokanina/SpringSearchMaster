package searchengine.dto.statistics;

import lombok.Value;

import java.util.List;

@Value
public class StatisticsData {
    private StatisticsTotal total;                       // Total statistics information
    private List<DetailedStatisticsItem> detailed;       // List of detailed statistics items
}
