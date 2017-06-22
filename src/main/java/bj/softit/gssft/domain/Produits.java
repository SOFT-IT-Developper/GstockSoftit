package bj.softit.gssft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Produits.
 */
@Entity
@Table(name = "produits")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "produits")
public class Produits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "emplacement")
    private String emplacement;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "capture")
    private byte[] capture;

    @Column(name = "capture_content_type")
    private String captureContentType;

    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OutStock> stocks = new HashSet<>();

    @OneToOne(mappedBy = "produit")
    @JsonIgnore
    private Stock stock;

    @ManyToOne
    private Categorie categorie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Produits name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public Produits emplacement(String emplacement) {
        this.emplacement = emplacement;
        return this;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getDescription() {
        return description;
    }

    public Produits description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCapture() {
        return capture;
    }

    public Produits capture(byte[] capture) {
        this.capture = capture;
        return this;
    }

    public void setCapture(byte[] capture) {
        this.capture = capture;
    }

    public String getCaptureContentType() {
        return captureContentType;
    }

    public Produits captureContentType(String captureContentType) {
        this.captureContentType = captureContentType;
        return this;
    }

    public void setCaptureContentType(String captureContentType) {
        this.captureContentType = captureContentType;
    }

    public Set<OutStock> getStocks() {
        return stocks;
    }

    public Produits stocks(Set<OutStock> outStocks) {
        this.stocks = outStocks;
        return this;
    }

    public Produits addStock(OutStock outStock) {
        this.stocks.add(outStock);
        outStock.setProduit(this);
        return this;
    }

    public Produits removeStock(OutStock outStock) {
        this.stocks.remove(outStock);
        outStock.setProduit(null);
        return this;
    }

    public void setStocks(Set<OutStock> outStocks) {
        this.stocks = outStocks;
    }

    public Stock getStock() {
        return stock;
    }

    public Produits stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Produits categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Produits produits = (Produits) o;
        if (produits.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), produits.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Produits{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", emplacement='" + getEmplacement() + "'" +
            ", description='" + getDescription() + "'" +
            ", capture='" + getCapture() + "'" +
            ", captureContentType='" + captureContentType + "'" +
            "}";
    }
}
