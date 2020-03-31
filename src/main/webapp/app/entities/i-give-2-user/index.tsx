import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IGive2User from './i-give-2-user';
import IGive2UserDetail from './i-give-2-user-detail';
import IGive2UserUpdate from './i-give-2-user-update';
import IGive2UserDeleteDialog from './i-give-2-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IGive2UserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IGive2UserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IGive2UserDetail} />
      <ErrorBoundaryRoute path={match.url} component={IGive2User} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IGive2UserDeleteDialog} />
  </>
);

export default Routes;
