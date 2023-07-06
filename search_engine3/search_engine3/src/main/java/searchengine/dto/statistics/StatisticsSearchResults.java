package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class StatisticsSearchResults {
    private boolean result;                    // Indicates the result of the search operation
    private int count;                         // Number of search results
    private List<StatisticsSearch> data;       // List of search results

    public StatisticsSearchResults(boolean result) {
        this.result = result;
    }
}
