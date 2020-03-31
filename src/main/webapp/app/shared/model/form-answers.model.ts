import { Moment } from 'moment';
import { IForm } from 'app/shared/model/form.model';
import { IAnswer } from 'app/shared/model/answer.model';
import { IParticipant } from 'app/shared/model/participant.model';

export interface IFormAnswers {
  id?: string;
  savedDate?: Moment;
  completed?: boolean;
  form?: IForm;
  responses?: IAnswer[];
  participant?: IParticipant;
}

export const defaultValue: Readonly<IFormAnswers> = {
  completed: false
};
