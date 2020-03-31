import { Moment } from 'moment';
import { IParticipantInstitution } from 'app/shared/model/participant-institution.model';
import { IResearcher } from 'app/shared/model/researcher.model';
import { State } from 'app/shared/model/enumerations/state.model';

export interface IStudy {
  id?: string;
  code?: string;
  iconContentType?: string;
  icon?: any;
  name?: string;
  description?: string;
  moreInfo?: string;
  contactEmail?: string;
  startDate?: Moment;
  endDate?: Moment;
  state?: State;
  recruiting?: boolean;
  requestedData?: string;
  dataJustification?: string;
  institutions?: IParticipantInstitution[];
  researcher?: IResearcher;
}

export const defaultValue: Readonly<IStudy> = {
  recruiting: false
};
