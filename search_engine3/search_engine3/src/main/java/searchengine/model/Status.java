package searchengine.model;

// Status is an enumeration that represents the status of a site page in a search engine.
public enum Status {
    // The site page is currently being indexed.
    INDEXING,

    // The site page has been successfully indexed.
    INDEXED,

    // The indexing of the site page has failed.
    FAILED
}
