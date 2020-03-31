package com.universaldoctor.igive2.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.universaldoctor.igive2.web.rest.TestUtil;

public class ParticipantInvitationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipantInvitation.class);
        ParticipantInvitation participantInvitation1 = new ParticipantInvitation();
        participantInvitation1.setId("id1");
        ParticipantInvitation participantInvitation2 = new ParticipantInvitation();
        participantInvitation2.setId(participantInvitation1.getId());
        assertThat(participantInvitation1).isEqualTo(participantInvitation2);
        participantInvitation2.setId("id2");
        assertThat(participantInvitation1).isNotEqualTo(participantInvitation2);
        participantInvitation1.setId(null);
        assertThat(participantInvitation1).isNotEqualTo(participantInvitation2);
    }
}
