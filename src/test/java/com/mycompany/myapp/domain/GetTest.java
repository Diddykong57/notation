package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Get.class);
        Get get1 = new Get();
        get1.setId(1L);
        Get get2 = new Get();
        get2.setId(get1.getId());
        assertThat(get1).isEqualTo(get2);
        get2.setId(2L);
        assertThat(get1).isNotEqualTo(get2);
        get1.setId(null);
        assertThat(get1).isNotEqualTo(get2);
    }
}
