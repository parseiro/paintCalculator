package com.vilelapinheiro.paintcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vilelapinheiro.paintcalculator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PortaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Porta.class);
        Porta porta1 = new Porta();
        porta1.setId(1L);
        Porta porta2 = new Porta();
        porta2.setId(porta1.getId());
        assertThat(porta1).isEqualTo(porta2);
        porta2.setId(2L);
        assertThat(porta1).isNotEqualTo(porta2);
        porta1.setId(null);
        assertThat(porta1).isNotEqualTo(porta2);
    }
}
