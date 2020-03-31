import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';

// prettier-ignore
import participant, {
  ParticipantState
} from 'app/entities/participant/participant.reducer';
// prettier-ignore
import form, {
  FormState
} from 'app/entities/form/form.reducer';
// prettier-ignore
import formQuestion, {
  FormQuestionState
} from 'app/entities/form-question/form-question.reducer';
// prettier-ignore
import iGive2User, {
  IGive2UserState
} from 'app/entities/i-give-2-user/i-give-2-user.reducer';
// prettier-ignore
import mobileUser, {
  MobileUserState
} from 'app/entities/mobile-user/mobile-user.reducer';
// prettier-ignore
import participantInvitation, {
  ParticipantInvitationState
} from 'app/entities/participant-invitation/participant-invitation.reducer';
// prettier-ignore
import study, {
  StudyState
} from 'app/entities/study/study.reducer';
// prettier-ignore
import participantInstitution, {
  ParticipantInstitutionState
} from 'app/entities/participant-institution/participant-institution.reducer';
// prettier-ignore
import formAnswers, {
  FormAnswersState
} from 'app/entities/form-answers/form-answers.reducer';
// prettier-ignore
import answer, {
  AnswerState
} from 'app/entities/answer/answer.reducer';
// prettier-ignore
import data, {
  DataState
} from 'app/entities/data/data.reducer';
// prettier-ignore
import researcher, {
  ResearcherState
} from 'app/entities/researcher/researcher.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly researcher: ResearcherState;
  readonly participant: ParticipantState;
  readonly form: FormState;
  readonly formQuestion: FormQuestionState;
  readonly iGive2User: IGive2UserState;
  readonly mobileUser: MobileUserState;
  readonly participantInvitation: ParticipantInvitationState;
  readonly study: StudyState;
  readonly participantInstitution: ParticipantInstitutionState;
  readonly formAnswers: FormAnswersState;
  readonly answer: AnswerState;
  readonly data: DataState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  researcher,
  participant,
  form,
  formQuestion,
  iGive2User,
  mobileUser,
  participantInvitation,
  study,
  participantInstitution,
  formAnswers,
  answer,
  data,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
