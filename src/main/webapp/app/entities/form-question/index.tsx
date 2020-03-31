import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FormQuestion from './form-question';
import FormQuestionDetail from './form-question-detail';
import FormQuestionUpdate from './form-question-update';
import FormQuestionDeleteDialog from './form-question-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FormQuestionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FormQuestionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FormQuestionDetail} />
      <ErrorBoundaryRoute path={match.url} component={FormQuestion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FormQuestionDeleteDialog} />
  </>
);

export default Routes;
