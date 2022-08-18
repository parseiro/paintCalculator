package com.vilelapinheiro.paintcalculator.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.vilelapinheiro.paintcalculator.domain.Parede} entity.
 */
public class ParedeDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal largura;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal altura;

    @Min(value = 0)
    @Max(value = 20)
    private Integer numPortas;

    @Min(value = 0)
    @Max(value = 100)
    private Integer numJanelas;

    private SalaDTO sala;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLargura() {
        return largura;
    }

    public void setLargura(BigDecimal largura) {
        this.largura = largura;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public Integer getNumPortas() {
        return numPortas;
    }

    public void setNumPortas(Integer numPortas) {
        this.numPortas = numPortas;
    }

    public Integer getNumJanelas() {
        return numJanelas;
    }

    public void setNumJanelas(Integer numJanelas) {
        this.numJanelas = numJanelas;
    }

    public SalaDTO getSala() {
        return sala;
    }

    public void setSala(SalaDTO sala) {
        this.sala = sala;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParedeDTO)) {
            return false;
        }

        ParedeDTO paredeDTO = (ParedeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paredeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParedeDTO{" +
            "id=" + getId() +
            ", largura=" + getLargura() +
            ", altura=" + getAltura() +
            ", numPortas=" + getNumPortas() +
            ", numJanelas=" + getNumJanelas() +
            ", sala=" + getSala() +
            "}";
    }
}
