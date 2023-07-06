package searchengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Lemma is an entity class that represents a lemma in a search engine.
// A lemma is the base form of a word. For example, the lemma of "running" is "run".
@Entity
@Getter
@Setter
@Table(name = "lemma", indexes = {@Index(name = "lemma_list", columnList = "lemma")})
@NoArgsConstructor
public class Lemma implements Serializable {

    // Unique identifier for the lemma.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Site page associated with the lemma.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private SitePage sitePageId;

    // The lemma string.
    private String lemma;

    // Frequency of the lemma.
    private int frequency;

    // List of index searches associated with the lemma.
    @OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL)
    private List<SearchIndex> index = new ArrayList<>();

    // Constructor that initializes the lemma, frequency, and sitePageId.
    public Lemma(String lemma, int frequency, SitePage sitePageId) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.sitePageId = sitePageId;
    }

    // Overrides the equals method to compare Lemma objects based on their fields.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lemma that = (Lemma) o;
        return id == that.id && frequency == that.frequency &&
                sitePageId.equals(that.sitePageId) &&
                lemma.equals(that.lemma) &&
                index.equals(that.index);
    }

    // Overrides the hashCode method to generate a hash code based on the fields of the Lemma object.
    @Override
    public int hashCode() {
        return Objects.hash(id, sitePageId, lemma, frequency, index);
    }

    // Returns a string representation of the Lemma object.
    @Override
    public String toString() {
        return "Lemma{" +
                "id=" + id +
                ", sitePageId=" + sitePageId +
                ", lemma='" + lemma + '\'' +
                ", frequency=" + frequency +
                ", index=" + index +
                '}';
    }
}
