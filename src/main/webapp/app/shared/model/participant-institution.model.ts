import { IStudy } from 'app/shared/model/study.model';

export interface IParticipantInstitution {
  id?: string;
  logoContentType?: string;
  logo?: any;
  study?: IStudy;
}

export const defaultValue: Readonly<IParticipantInstitution> = {};
