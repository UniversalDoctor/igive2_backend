import { IFormQuestion } from 'app/shared/model/form-question.model';
import { IFormAnswers } from 'app/shared/model/form-answers.model';

export interface IAnswer {
  id?: string;
  data?: string;
  formQuestion?: IFormQuestion;
  formAnswers?: IFormAnswers;
}

export const defaultValue: Readonly<IAnswer> = {};
