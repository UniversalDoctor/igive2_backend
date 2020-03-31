import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Researcher from './researcher';
import Participant from './participant';
import Form from './form';
import FormQuestion from './form-question';
import IGive2User from './i-give-2-user';
import MobileUser from './mobile-user';
import ParticipantInvitation from './participant-invitation';
import Study from './study';
import ParticipantInstitution from './participant-institution';
import FormAnswers from './form-answers';
import Answer from './answer';
import Data from './data';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}researcher`} component={Researcher} />
      <ErrorBoundaryRoute path={`${match.url}participant`} component={Participant} />
      <ErrorBoundaryRoute path={`${match.url}form`} component={Form} />
      <ErrorBoundaryRoute path={`${match.url}form-question`} component={FormQuestion} />
      <ErrorBoundaryRoute path={`${match.url}i-give-2-user`} component={IGive2User} />
      <ErrorBoundaryRoute path={`${match.url}mobile-user`} component={MobileUser} />
      <ErrorBoundaryRoute path={`${match.url}participant-invitation`} component={ParticipantInvitation} />
      <ErrorBoundaryRoute path={`${match.url}study`} component={Study} />
      <ErrorBoundaryRoute path={`${match.url}participant-institution`} component={ParticipantInstitution} />
      <ErrorBoundaryRoute path={`${match.url}form-answers`} component={FormAnswers} />
      <ErrorBoundaryRoute path={`${match.url}answer`} component={Answer} />
      <ErrorBoundaryRoute path={`${match.url}data`} component={Data} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
