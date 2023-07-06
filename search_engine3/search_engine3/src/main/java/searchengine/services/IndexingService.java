package searchengine.services;

public interface IndexingService {
    // Method to perform URL indexing
    boolean urlIndexing(String url);

    // Method to perform indexing for all URLs
    boolean indexingAll();

    // Method to stop the indexing process
    boolean stopIndexing();
}
