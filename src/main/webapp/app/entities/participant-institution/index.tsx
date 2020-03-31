import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ParticipantInstitution from './participant-institution';
import ParticipantInstitutionDetail from './participant-institution-detail';
import ParticipantInstitutionUpdate from './participant-institution-update';
import ParticipantInstitutionDeleteDialog from './participant-institution-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParticipantInstitutionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParticipantInstitutionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParticipantInstitutionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ParticipantInstitution} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ParticipantInstitutionDeleteDialog} />
  </>
);

export default Routes;
