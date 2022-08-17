package com.vilelapinheiro.paintcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vilelapinheiro.paintcalculator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParedeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parede.class);
        Parede parede1 = new Parede();
        parede1.setId(1L);
        Parede parede2 = new Parede();
        parede2.setId(parede1.getId());
        assertThat(parede1).isEqualTo(parede2);
        parede2.setId(2L);
        assertThat(parede1).isNotEqualTo(parede2);
        parede1.setId(null);
        assertThat(parede1).isNotEqualTo(parede2);
    }
}
