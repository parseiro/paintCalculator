package com.vilelapinheiro.paintcalculator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Sala.
 */
@Entity
@Table(name = "sala")
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "sala")
    @JsonIgnoreProperties(value = { "sala" }, allowSetters = true)
    private Set<Parede> paredes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sala id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Sala nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Parede> getParedes() {
        return this.paredes;
    }

    public void setParedes(Set<Parede> paredes) {
        if (this.paredes != null) {
            this.paredes.forEach(i -> i.setSala(null));
        }
        if (paredes != null) {
            paredes.forEach(i -> i.setSala(this));
        }
        this.paredes = paredes;
    }

    public Sala paredes(Set<Parede> paredes) {
        this.setParedes(paredes);
        return this;
    }

    public Sala addParedes(Parede parede) {
        this.paredes.add(parede);
        parede.setSala(this);
        return this;
    }

    public Sala removeParedes(Parede parede) {
        this.paredes.remove(parede);
        parede.setSala(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sala)) {
            return false;
        }
        return id != null && id.equals(((Sala) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sala{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
