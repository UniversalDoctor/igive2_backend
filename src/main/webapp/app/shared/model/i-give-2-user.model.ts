import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { IResearcher } from 'app/shared/model/researcher.model';
import { IUser } from 'app/shared/model/user.model';

export interface IIGive2User {
  id?: string;
  newsletter?: boolean;
  termsAccepted?: boolean;
  country?: string;
  mobileUser?: IMobileUser;
  researcher?: IResearcher;
  user?: IUser;
}

export const defaultValue: Readonly<IIGive2User> = {
  newsletter: false,
  termsAccepted: false
};

/** *****origianl****
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { IResearcher } from 'app/shared/model/researcher.model';

export interface IIGive2User {
  id?: string;
  newsletter?: boolean;
  termsAccepted?: boolean;
  country?: string;
  mobileUser?: IMobileUser;
  researcher?: IResearcher;
}

export const defaultValue: Readonly<IIGive2User> = {
  newsletter: false,
  termsAccepted: false
};
*/
