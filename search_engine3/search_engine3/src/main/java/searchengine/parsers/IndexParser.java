package searchengine.parsers;

import searchengine.dto.statistics.IndexStatistics;
import searchengine.model.SitePage;

import java.util.List;

public interface IndexParser {
    void run(SitePage site);
    List<IndexStatistics> getIndexList();
}
