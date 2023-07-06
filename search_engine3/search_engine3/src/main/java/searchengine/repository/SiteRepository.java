package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.SitePage;

@Repository
public interface SiteRepository extends JpaRepository<SitePage, Long> {
    // Find a SitePage by URL
    SitePage findByUrl(String url);

    // Find a SitePage by ID
    SitePage findByUrl(long id);

    // Find a SitePage by SitePage object
    SitePage findByUrl(SitePage site);
}
