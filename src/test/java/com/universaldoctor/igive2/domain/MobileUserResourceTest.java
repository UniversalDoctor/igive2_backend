package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class MobileUserResourceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileUser.class);
        MobileUser mobileUser1 = new MobileUser();
        mobileUser1.setId("id1");
        MobileUser mobileUser2 = new MobileUser();
        mobileUser2.setId(mobileUser1.getId());
        assertThat(mobileUser1).isEqualTo(mobileUser2);
        mobileUser2.setId("id2");
        assertThat(mobileUser1).isNotEqualTo(mobileUser2);
        mobileUser1.setId(null);
        assertThat(mobileUser1).isNotEqualTo(mobileUser2);
    }
}
