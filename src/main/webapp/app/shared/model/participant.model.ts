import { Moment } from 'moment';
import { IData } from 'app/shared/model/data.model';
import { IFormAnswers } from 'app/shared/model/form-answers.model';
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { IStudy } from 'app/shared/model/study.model';

export interface IParticipant {
  id?: string;
  entryDate?: Moment;
  anonymousId?: string;
  participantData?: IData[];
  forms?: IFormAnswers[];
  mobileUser?: IMobileUser;
  study?: IStudy;
}

export const defaultValue: Readonly<IParticipant> = {};
