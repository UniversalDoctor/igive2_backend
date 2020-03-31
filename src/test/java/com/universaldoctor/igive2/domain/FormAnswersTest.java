package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class FormAnswersTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormAnswers.class);
        FormAnswers formAnswers1 = new FormAnswers();
        formAnswers1.setId("id1");
        FormAnswers formAnswers2 = new FormAnswers();
        formAnswers2.setId(formAnswers1.getId());
        assertThat(formAnswers1).isEqualTo(formAnswers2);
        formAnswers2.setId("id2");
        assertThat(formAnswers1).isNotEqualTo(formAnswers2);
        formAnswers1.setId(null);
        assertThat(formAnswers1).isNotEqualTo(formAnswers2);
    }
}
