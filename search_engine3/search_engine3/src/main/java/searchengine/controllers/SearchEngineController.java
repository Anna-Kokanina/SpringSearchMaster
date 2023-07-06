package searchengine.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.statistics.*;
import searchengine.repository.SiteRepository;
import searchengine.services.IndexingService;
import searchengine.services.SearchService;
import searchengine.services.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Search engine API controller", description = "Whole site indexing, certain site reindexing, "
        + "stop indexing, search, site statistics")
public class SearchEngineController {

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SiteRepository siteRepository;
    private final SearchService searchService;

    public SearchEngineController(StatisticsService statisticsService, IndexingService indexingService, SiteRepository siteRepository, SearchService searchService) {
        this.statisticsService = statisticsService;
        this.indexingService = indexingService;
        this.siteRepository = siteRepository;
        this.searchService = searchService;
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get site statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        // Fetch and return site statistics
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    @Operation(summary = "Site indexing")
    public ResponseEntity<Object> startIndexing() {
        // Start indexing all sites
        return indexingService.indexingAll() ? createResponse(true, null) : createStatisticsRequest(false, "Indexing not started");
    }

    @GetMapping("/stopIndexing")
    @Operation(summary = "Stop indexing")
    public ResponseEntity<Object> stopIndexing() {
        // Stop site indexing
        return indexingService.stopIndexing() ? createResponse(true, null) : createStatisticsRequest(false, "Indexing was not stopped because it was not started");
    }

    @GetMapping("/search")
    @Operation(summary = "Search")
    public ResponseEntity<Object> search(
            @RequestParam(name = "query", required = false, defaultValue = "") String request,
            @RequestParam(name = "site", required = false, defaultValue = "") String site,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit) {
        // Perform search based on provided parameters
        return request.isEmpty() ? createStatisticsRequest(false, "Empty request") : performSearch(request, site, offset, limit);
    }

    @PostMapping("/indexPage")
    @Operation(summary = "Certain page indexing")
    public ResponseEntity<Object> indexPage(@RequestParam(name = "url") String url) {
        // Index a specific page
        return url.isEmpty() ? createStatisticsRequest(false, "Page not specified") : indexSpecificPage(url);
    }

    private ResponseEntity<Object> createResponse(boolean success, String message) {
        // Create a response entity with the provided success status and message
        return new ResponseEntity<>(new Response(success), HttpStatus.OK);
    }

    private ResponseEntity<Object> createStatisticsRequest(boolean success, String message) {
        // Create a statistics request entity with the provided success status and message
        return new ResponseEntity<>(new StatisticsRequest(success, message), HttpStatus.OK);
    }

    private ResponseEntity<Object> performSearch(String request, String site, int offset, int limit) {
        // Perform search on a```java specific site if provided, or on all sites if not
        List<StatisticsSearch> searchData;
        if (!site.isEmpty()) {
            if (siteRepository.findByUrl(site) == null) {
                // Site not found in repository
                return createStatisticsRequest(false, "Required page not found");
            } else {
                // Perform search on a specific site
                searchData = searchService.siteSearch(request, site, offset, limit);
            }
        } else {
            // Perform search on all sites
            searchData = searchService.allSiteSearch(request, offset, limit);
        }
        return new ResponseEntity<>(new StatisticsSearchResults(true, searchData.size(), searchData), HttpStatus.OK);
    }

    private ResponseEntity<Object> indexSpecificPage(String url) {
        // Index a specific page and log the result
        if (indexingService.urlIndexing(url)) {
            log.info("Page - " + url + " - added for reindexing");
            // Page added for reindexing
            return createResponse(true, null);
        } else {
            log.info("Required page out of configuration file");
            // Page URL not found in configuration file
            return createStatisticsRequest(false, "Required page out of configuration file");
        }
    }
}
