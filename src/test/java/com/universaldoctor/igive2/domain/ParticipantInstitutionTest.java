package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class ParticipantInstitutionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipantInstitution.class);
        ParticipantInstitution participantInstitution1 = new ParticipantInstitution();
        participantInstitution1.setId("id1");
        ParticipantInstitution participantInstitution2 = new ParticipantInstitution();
        participantInstitution2.setId(participantInstitution1.getId());
        assertThat(participantInstitution1).isEqualTo(participantInstitution2);
        participantInstitution2.setId("id2");
        assertThat(participantInstitution1).isNotEqualTo(participantInstitution2);
        participantInstitution1.setId(null);
        assertThat(participantInstitution1).isNotEqualTo(participantInstitution2);
    }
}
