import { Moment } from 'moment';
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { IParticipant } from 'app/shared/model/participant.model';
import { DataType } from 'app/shared/model/enumerations/data-type.model';

export interface IData {
  id?: string;
  data?: DataType;
  notes?: string;
  date?: Moment;
  value?: string;
  mobileUser?: IMobileUser;
  participant?: IParticipant;
}

export const defaultValue: Readonly<IData> = {};
