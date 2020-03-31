import { IFormQuestion } from 'app/shared/model/form-question.model';
import { IFormAnswers } from 'app/shared/model/form-answers.model';
import { IStudy } from 'app/shared/model/study.model';
import { State } from 'app/shared/model/enumerations/state.model';

export interface IForm {
  id?: string;
  name?: string;
  description?: string;
  state?: State;
  questions?: IFormQuestion[];
  formAnswers?: IFormAnswers;
  study?: IStudy;
}

export const defaultValue: Readonly<IForm> = {};
