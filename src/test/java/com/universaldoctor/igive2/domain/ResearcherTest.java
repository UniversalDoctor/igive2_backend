package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class ResearcherTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Researcher.class);
        Researcher researcher1 = new Researcher();
        researcher1.setId("id1");
        Researcher researcher2 = new Researcher();
        researcher2.setId(researcher1.getId());
        assertThat(researcher1).isEqualTo(researcher2);
        researcher2.setId("id2");
        assertThat(researcher1).isNotEqualTo(researcher2);
        researcher1.setId(null);
        assertThat(researcher1).isNotEqualTo(researcher2);
    }
}
