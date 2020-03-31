import { IAnswer } from 'app/shared/model/answer.model';
import { IForm } from 'app/shared/model/form.model';
import { QuestionType } from 'app/shared/model/enumerations/question-type.model';

export interface IFormQuestion {
  id?: string;
  question?: string;
  isMandatory?: boolean;
  type?: QuestionType;
  options?: string;
  answer?: IAnswer;
  form?: IForm;
}

export const defaultValue: Readonly<IFormQuestion> = {
  isMandatory: false
};
