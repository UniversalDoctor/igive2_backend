import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { IStudy } from 'app/shared/model/study.model';

export interface IResearcher {
  id?: string;
  institution?: string;
  honorifics?: string;
  userId?: string;
  iGive2User?: IIGive2User;
  studies?: IStudy[];
}

export const defaultValue: Readonly<IResearcher> = {};
