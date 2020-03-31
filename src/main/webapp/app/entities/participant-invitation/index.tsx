import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ParticipantInvitation from './participant-invitation';
import ParticipantInvitationDetail from './participant-invitation-detail';
import ParticipantInvitationUpdate from './participant-invitation-update';
import ParticipantInvitationDeleteDialog from './participant-invitation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParticipantInvitationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParticipantInvitationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParticipantInvitationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ParticipantInvitation} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ParticipantInvitationDeleteDialog} />
  </>
);

export default Routes;
