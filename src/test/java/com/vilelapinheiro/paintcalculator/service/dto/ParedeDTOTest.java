package com.vilelapinheiro.paintcalculator.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vilelapinheiro.paintcalculator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParedeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParedeDTO.class);
        ParedeDTO paredeDTO1 = new ParedeDTO();
        paredeDTO1.setId(1L);
        ParedeDTO paredeDTO2 = new ParedeDTO();
        assertThat(paredeDTO1).isNotEqualTo(paredeDTO2);
        paredeDTO2.setId(paredeDTO1.getId());
        assertThat(paredeDTO1).isEqualTo(paredeDTO2);
        paredeDTO2.setId(2L);
        assertThat(paredeDTO1).isNotEqualTo(paredeDTO2);
        paredeDTO1.setId(null);
        assertThat(paredeDTO1).isNotEqualTo(paredeDTO2);
    }
}
