import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FormAnswers from './form-answers';
import FormAnswersDetail from './form-answers-detail';
import FormAnswersUpdate from './form-answers-update';
import FormAnswersDeleteDialog from './form-answers-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FormAnswersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FormAnswersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FormAnswersDetail} />
      <ErrorBoundaryRoute path={match.url} component={FormAnswers} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FormAnswersDeleteDialog} />
  </>
);

export default Routes;
