package searchengine.services;

import searchengine.dto.statistics.StatisticsSearch;

import java.util.List;

public interface SearchService {
    // Method to perform a search across all sites
    List<StatisticsSearch> allSiteSearch(String text, int offset, int limit);

    // Method to perform a search within a specific site URL
    List<StatisticsSearch> siteSearch(String searchText, String url, int offset, int limit);
}
