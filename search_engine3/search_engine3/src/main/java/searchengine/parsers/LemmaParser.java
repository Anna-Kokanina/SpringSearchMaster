package searchengine.parsers;

import searchengine.dto.statistics.LemmaStatistics;
import searchengine.model.SitePage;

import java.util.List;

public interface LemmaParser {
    void run(SitePage site);
    List<LemmaStatistics> getLemmaDtoList();
}
