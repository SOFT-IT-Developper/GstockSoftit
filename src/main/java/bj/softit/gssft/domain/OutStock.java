package bj.softit.gssft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A OutStock.
 */
@Entity
@Table(name = "out_stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "outstock")
public class OutStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantite", precision=10, scale=2, nullable = false)
    private BigDecimal quantite;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "cause")
    private String cause;

    @ManyToOne(optional = false)
    @NotNull
    private Produits produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public OutStock quantite(BigDecimal quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDate() {
        return date;
    }

    public OutStock date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCause() {
        return cause;
    }

    public OutStock cause(String cause) {
        this.cause = cause;
        return this;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Produits getProduit() {
        return produit;
    }

    public OutStock produit(Produits produits) {
        this.produit = produits;
        return this;
    }

    public void setProduit(Produits produits) {
        this.produit = produits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutStock outStock = (OutStock) o;
        if (outStock.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), outStock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OutStock{" +
            "id=" + getId() +
            ", quantite='" + getQuantite() + "'" +
            ", date='" + getDate() + "'" +
            ", cause='" + getCause() + "'" +
            "}";
    }
}
