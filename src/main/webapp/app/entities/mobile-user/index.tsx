import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MobileUser from './mobile-user';
import MobileUserDetail from './mobile-user-detail';
import MobileUserUpdate from './mobile-user-update';
import MobileUserDeleteDialog from './mobile-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MobileUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MobileUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MobileUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={MobileUser} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MobileUserDeleteDialog} />
  </>
);

export default Routes;
