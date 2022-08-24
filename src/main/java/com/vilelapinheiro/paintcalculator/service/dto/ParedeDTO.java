package com.vilelapinheiro.paintcalculator.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.vilelapinheiro.paintcalculator.domain.Parede} entity.
 */
public class ParedeDTO implements Serializable {

    public static final BigDecimal JANELA_LARGURA = BigDecimal.valueOf(2);
    public static final BigDecimal JANELA_ALTURA = BigDecimal.valueOf(1.2);
    public static final BigDecimal PORTA_LARGURA = BigDecimal.valueOf(0.8);
    public static final BigDecimal PORTA_ALTURA = BigDecimal.valueOf(1.9);
    public static final BigDecimal PAREDE_MAIS_ALTA_QUE_PORTA = BigDecimal.valueOf(0.3);
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
    @PositiveOrZero
    @DecimalMax(value = "0.50")
    public BigDecimal getAreaProporcionalPortasJanelasMaiorQue50Porcento() {
        // Regra: Cada janela possui as medidas: 2,00 x 1,20 mtos
        final var AREA_JANELA = JANELA_LARGURA.multiply(JANELA_ALTURA);

        // Regra: Cada porta possui as medidas: 0,80 x 1,90
        final var AREA_PORTA = PORTA_LARGURA.multiply(PORTA_ALTURA);

        final Integer janelas = Objects.requireNonNullElse(this.getNumJanelas(), 0);
        final var areaJanelas = AREA_JANELA.multiply(BigDecimal.valueOf(janelas));

        final Integer portas = Objects.requireNonNullElse(this.getNumPortas(), 0);
        final var areaPortas = AREA_PORTA.multiply(BigDecimal.valueOf(portas));

        final var areaPortasEJanelas = areaJanelas.add(areaPortas);
        if (areaPortasEJanelas.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        final var areaTotalParede = Objects.requireNonNullElse(this.getAreaTotal(), BigDecimal.ZERO);

        final var valor = areaPortasEJanelas.divide(areaTotalParede, 2, RoundingMode.HALF_EVEN);

        return valor;
    }

    /*    @AssertFalse
    public Boolean getParedeEstreitaDemais() {
        if (this.numPortas > 0) {
            if (this.largura.compareTo(PORTA_LARGURA) >= 0) {
                // tem largura suficiente
                return false;
            }
            return true;
        }

        // nao tem nenhuma porta
        return false;
    }*/

    @AssertFalse
    public Boolean getParedeBaixaDemais() {
        if (this.numPortas > 0) {
            if (this.altura.subtract(PORTA_ALTURA).compareTo(PAREDE_MAIS_ALTA_QUE_PORTA) >= 0) {
                // tem altura suficiente
                return false;
            }
            return true;
        }

        // nao tem nenhuma porta
        return false;
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
