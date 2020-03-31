import { IStudy } from 'app/shared/model/study.model';

export interface IParticipantInvitation {
  id?: string;
  email?: string;
  state?: boolean;
  participantId?: string;
  study?: IStudy;
}

export const defaultValue: Readonly<IParticipantInvitation> = {
  state: false
};
