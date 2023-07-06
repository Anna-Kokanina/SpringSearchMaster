package searchengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

// SitePage is an entity class that represents a site page in a search engine.
@Entity
@Setter
@Getter
@Table(name = "site")
public class SitePage {

    // Unique identifier for the site page.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Status of the site page.
    @Enumerated(EnumType.STRING)
    private Status status;

    // Time of the status.
    @Column(name = "status_time")
    private Date statusTime;

    // Last error message associated with the site page.
    @Column(name = "last_error")
    private String lastError;

    // URL of the site page.
    private String url;

    // Name of the site page.
    private String name;

    // List of pages associated with the site page.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "siteId", cascade = CascadeType.ALL)
    protected List<Page> pageList = new ArrayList<>();

    // List of lemmas associated with the site page.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sitePageId", cascade = CascadeType.ALL)
    protected List<Lemma> lemmaEntityList = new ArrayList<>();

    // Overrides the equals method to compare SitePage objects based on their fields.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SitePage that = (SitePage) o;
        return id == that.id && status == that.status &&
                statusTime.equals(that.statusTime) &&
                Objects.equals(lastError, that.lastError) &&
                url.equals(that.url) && name.equals(that.name);
    }

    // Overrides the hashCode method to generate a hash code based on the fields of the SitePage object.
    @Override
    public int hashCode() {
        return Objects.hash(id, status, statusTime, lastError, url, name);
    }
}
// Returns a string representation of the
