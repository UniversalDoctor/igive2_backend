import { Moment } from 'moment';
import { IParticipant } from 'app/shared/model/participant.model';
import { IForm } from 'app/shared/model/form.model';
import { IInstitution } from 'app/shared/model/institution.model';
import { IResearcher } from 'app/shared/model/researcher.model';
import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { ResearchState } from 'app/shared/model/enumerations/research-state.model';

export interface IResearch {
  id?: string;
  iconContentType?: string;
  icon?: any;
  name?: string;
  description?: string;
  moreInfo?: string;
  dataRequested?: DataType;
  contactEmail?: string;
  startDate?: Moment;
  endDate?: Moment;
  state?: ResearchState;
  recruiting?: boolean;
  participants?: IParticipant[];
  forms?: IForm[];
  institutions?: IInstitution[];
  researcher?: IResearcher;
}

export const defaultValue: Readonly<IResearch> = {
  recruiting: false
};
