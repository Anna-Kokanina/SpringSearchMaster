package searchengine.dto.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StatisticsSearch {
    private String site;         // Site associated with the search result
    private String siteName;     // Name of the site
    private String uri;          // URI of the search result
    private String title;        // Title of the search result
    private String snippet;      // Snippet or summary of the search result
    private Float relevance;     // Relevance score of the search result

    public StatisticsSearch(String site, String siteName, String uri,
                            String title, String snippet, Float relevance) {
        this.site = site;
        this.siteName = siteName;
        this.uri = uri;
        this.title = title;
        this.snippet = snippet;
        this.relevance = relevance;
    }
}
