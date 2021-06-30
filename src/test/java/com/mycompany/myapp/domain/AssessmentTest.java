package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssessmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assessment.class);
        Assessment assessment1 = new Assessment();
        assessment1.setId(1L);
        Assessment assessment2 = new Assessment();
        assessment2.setId(assessment1.getId());
        assertThat(assessment1).isEqualTo(assessment2);
        assessment2.setId(2L);
        assertThat(assessment1).isNotEqualTo(assessment2);
        assessment1.setId(null);
        assertThat(assessment1).isNotEqualTo(assessment2);
    }
}
