import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Researcher from './researcher';
import ResearcherDetail from './researcher-detail';
import ResearcherUpdate from './researcher-update';
import ResearcherDeleteDialog from './researcher-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ResearcherUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ResearcherUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ResearcherDetail} />
      <ErrorBoundaryRoute path={match.url} component={Researcher} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ResearcherDeleteDialog} />
  </>
);

export default Routes;
