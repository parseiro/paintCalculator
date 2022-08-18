package com.vilelapinheiro.paintcalculator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Parede.
 */
@Entity
@Table(name = "parede")
public class Parede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "largura", precision = 4, scale = 2, nullable = false)
    private BigDecimal largura = BigDecimal.valueOf(2);

    @NotNull
    @Column(name = "altura", precision = 4, scale = 2, nullable = false)
    private BigDecimal altura = BigDecimal.valueOf(2);

    @Min(value = 0)
    @Column(name = "num_portas")
    private Integer numPortas;

    @Min(value = 0)
    @Column(name = "num_janelas")
    private Integer numJanelas;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paredes" }, allowSetters = true)
    private Sala sala;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parede id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLargura() {
        return this.largura;
    }

    public Parede largura(BigDecimal largura) {
        this.setLargura(largura);
        return this;
    }

    public void setLargura(BigDecimal largura) {
        this.largura = largura;
    }

    public BigDecimal getAltura() {
        return this.altura;
    }

    public Parede altura(BigDecimal altura) {
        this.setAltura(altura);
        return this;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public Integer getNumPortas() {
        return this.numPortas;
    }

    public Parede numPortas(Integer numPortas) {
        this.setNumPortas(numPortas);
        return this;
    }

    public void setNumPortas(Integer numPortas) {
        this.numPortas = numPortas;
    }

    public Integer getNumJanelas() {
        return this.numJanelas;
    }

    public Parede numJanelas(Integer numJanelas) {
        this.setNumJanelas(numJanelas);
        return this;
    }

    public void setNumJanelas(Integer numJanelas) {
        this.numJanelas = numJanelas;
    }

    public Sala getSala() {
        return this.sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Parede sala(Sala sala) {
        this.setSala(sala);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parede)) {
            return false;
        }
        return id != null && id.equals(((Parede) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parede{" +
            "id=" + getId() +
            ", largura=" + getLargura() +
            ", altura=" + getAltura() +
            ", numPortas=" + getNumPortas() +
            ", numJanelas=" + getNumJanelas() +
            "}";
    }
}
