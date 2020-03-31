package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class IGive2UserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IGive2User.class);
        IGive2User iGive2User1 = new IGive2User();
        iGive2User1.setId("id1");
        IGive2User iGive2User2 = new IGive2User();
        iGive2User2.setId(iGive2User1.getId());
        assertThat(iGive2User1).isEqualTo(iGive2User2);
        iGive2User2.setId("id2");
        assertThat(iGive2User1).isNotEqualTo(iGive2User2);
        iGive2User1.setId(null);
        assertThat(iGive2User1).isNotEqualTo(iGive2User2);
    }
}
