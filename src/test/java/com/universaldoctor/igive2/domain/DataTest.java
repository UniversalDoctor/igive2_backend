package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class DataTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Data.class);
        Data data1 = new Data();
        data1.setId("id1");
        Data data2 = new Data();
        data2.setId(data1.getId());
        assertThat(data1).isEqualTo(data2);
        data2.setId("id2");
        assertThat(data1).isNotEqualTo(data2);
        data1.setId(null);
        assertThat(data1).isNotEqualTo(data2);
    }
}
