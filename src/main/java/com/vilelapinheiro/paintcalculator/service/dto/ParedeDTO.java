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
    @Positive
    @DecimalMin(value = "0")
    private BigDecimal largura;

    @NotNull
    @Positive
    @DecimalMin(value = "0")
    private BigDecimal altura;

    @NotNull
    @Min(value = 0)
    private Integer numPortas;

    @NotNull
    @Min(value = 0)
    private Integer numJanelas;

    private SalaDTO sala;

    // Regra: Nenhuma parede pode ter menos de 1 metro quadrado nem mais de 50 metros quadrados,
    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "50")
    public BigDecimal getAreaTotal() {
        if (this.largura == null || this.altura == null) {
            return BigDecimal.ZERO;
        }

        return this.largura.multiply(this.altura);
    }

    // Regra: O total de área das portas e janelas deve ser no máximo 50% da área de parede
    @DecimalMin(value = "0")
    @DecimalMax(value = "0.5")
    public BigDecimal getAreaProporcionalPortasJanelas() {
        // Regra: Cada janela possui as medidas: 2,00 x 1,20 mtos
        final var AREA_JANELA = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(1.2));

        // Regra: Cada porta possui as medidas: 0,80 x 1,90
        final var AREA_PORTA = BigDecimal.valueOf(0.8).multiply(BigDecimal.valueOf(1.9));

        final Integer janelas = Objects.requireNonNullElse(this.getNumJanelas(), 0);
        final var areaJanelas = AREA_JANELA.multiply(BigDecimal.valueOf(janelas));

        final Integer portas = Objects.requireNonNullElse(this.getNumPortas(), 0);
        final var areaPortas = AREA_PORTA.multiply(BigDecimal.valueOf(portas));

        final var areaPortasEJanelas = areaJanelas.add(areaPortas);
        if (areaPortasEJanelas.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        final var areaTotalParede = Objects.requireNonNullElse(this.getAreaTotal(), BigDecimal.ZERO);

        return areaPortasEJanelas.setScale(2).divide(areaTotalParede.setScale(2));
    }

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
