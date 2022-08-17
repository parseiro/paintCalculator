package com.vilelapinheiro.paintcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vilelapinheiro.paintcalculator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JanelaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Janela.class);
        Janela janela1 = new Janela();
        janela1.setId(1L);
        Janela janela2 = new Janela();
        janela2.setId(janela1.getId());
        assertThat(janela1).isEqualTo(janela2);
        janela2.setId(2L);
        assertThat(janela1).isNotEqualTo(janela2);
        janela1.setId(null);
        assertThat(janela1).isNotEqualTo(janela2);
    }
}
