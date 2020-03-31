import { Moment } from 'moment';
import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { IParticipant } from 'app/shared/model/participant.model';
import { GenderType } from 'app/shared/model/enumerations/gender-type.model';
import { Diseases } from 'app/shared/model/enumerations/diseases.model';

export interface IMobileUser {
  id?: string;
  gender?: GenderType;
  birthdate?: Moment;
  diseases?: Diseases;
  userId?: string;
  status?: string;
  iconContentType?: string;
  icon?: any;
  username?: string;
  iGive2User?: IIGive2User;
  studies?: IParticipant[];
}

export const defaultValue: Readonly<IMobileUser> = {};
