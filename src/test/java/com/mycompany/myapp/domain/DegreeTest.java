package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DegreeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Degree.class);
        Degree degree1 = new Degree();
        degree1.setId(1L);
        Degree degree2 = new Degree();
        degree2.setId(degree1.getId());
        assertThat(degree1).isEqualTo(degree2);
        degree2.setId(2L);
        assertThat(degree1).isNotEqualTo(degree2);
        degree1.setId(null);
        assertThat(degree1).isNotEqualTo(degree2);
    }
}
