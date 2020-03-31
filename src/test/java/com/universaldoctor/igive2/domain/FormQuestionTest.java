package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class FormQuestionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormQuestion.class);
        FormQuestion formQuestion1 = new FormQuestion();
        formQuestion1.setId("id1");
        FormQuestion formQuestion2 = new FormQuestion();
        formQuestion2.setId(formQuestion1.getId());
        assertThat(formQuestion1).isEqualTo(formQuestion2);
        formQuestion2.setId("id2");
        assertThat(formQuestion1).isNotEqualTo(formQuestion2);
        formQuestion1.setId(null);
        assertThat(formQuestion1).isNotEqualTo(formQuestion2);
    }
}
