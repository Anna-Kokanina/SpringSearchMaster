package searchengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

// SearchIndex is an entity class that represents an index search in a search engine.
// It associates a Page and a Lemma with a rank.
@Entity
@Getter
@Setter
@Table(name = "index_search", indexes = {
        @Index(name = "page_id_list", columnList = "page_id"),
        @Index(name = "lemma_id_list", columnList = "lemma_id")
})
@NoArgsConstructor
public class SearchIndex implements Serializable {

    // Unique identifier for the index search entity.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Page associated with the index search entity.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;

    // Lemma associated with the index search entity.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id")
    private Lemma lemma;

    // Rank of the index search entity.
    @Column(nullable = false, name = "index_rank")
    private float rank;

    // Constructor that initializes the page, lemma, and rank.
    public SearchIndex(Page page, Lemma lemma, float rank) {
        this.page = page;
        this.lemma = lemma;
        this.rank = rank;
    }

    // Overrides the equals method to compare SearchIndex objects based on their fields.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchIndex that = (SearchIndex) o;
        return id == that.id && Float.compare(that.rank, rank) == 0 && page.equals(that.page)
                && lemma.equals(that.lemma);
    }

    // Overrides the hashCode method to generate a hash code based on the fields of the SearchIndex object.
    @Override
    public int hashCode() {
        return Objects.hash(id, page, lemma, rank);
    }

    // Returns a string representation of the SearchIndex object.
    @Override
    public String toString() {
        return "IndexEntity{" +
                "id=" + id +
                ", page=" + page +
                ", lemma=" + lemma +
                ", rank=" + rank +
                '}';
    }
}
