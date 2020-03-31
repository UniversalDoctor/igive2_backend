import { IResearch } from 'app/shared/model/research.model';

export interface IInstitution {
  id?: string;
  name?: string;
  webPage?: string;
  logoContentType?: string;
  logo?: any;
  researchs?: IResearch[];
}

export const defaultValue: Readonly<IInstitution> = {};
