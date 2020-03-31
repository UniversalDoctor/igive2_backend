import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFormQuestion } from 'app/shared/model/form-question.model';
import { getEntities as getFormQuestions } from 'app/entities/form-question/form-question.reducer';
import { IFormAnswers } from 'app/shared/model/form-answers.model';
import { getEntities as getFormAnswers } from 'app/entities/form-answers/form-answers.reducer';
import { getEntity, updateEntity, createEntity, reset } from './answer.reducer';
import { IAnswer } from 'app/shared/model/answer.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnswerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAnswerUpdateState {
  isNew: boolean;
  formQuestionId: string;
  formAnswersId: string;
}

export class AnswerUpdate extends React.Component<IAnswerUpdateProps, IAnswerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      formQuestionId: '0',
      formAnswersId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getFormQuestions();
    this.props.getFormAnswers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { answerEntity } = this.props;
      const entity = {
        ...answerEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/answer');
  };

  render() {
    const { answerEntity, formQuestions, formAnswers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.answer.home.createOrEditLabel">
              <Translate contentKey="igive2App.answer.home.createOrEditLabel">Create or edit a Answer</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : answerEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="answer-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="answer-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dataLabel" for="answer-data">
                    <Translate contentKey="igive2App.answer.data">Data</Translate>
                  </Label>
                  <AvField
                    id="answer-data"
                    type="text"
                    name="data"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="answer-formQuestion">
                    <Translate contentKey="igive2App.answer.formQuestion">Form Question</Translate>
                  </Label>
                  <AvInput id="answer-formQuestion" type="select" className="form-control" name="formQuestion.id">
                    <option value="" key="0" />
                    {formQuestions
                      ? formQuestions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="answer-formAnswers">
                    <Translate contentKey="igive2App.answer.formAnswers">Form Answers</Translate>
                  </Label>
                  <AvInput id="answer-formAnswers" type="select" className="form-control" name="formAnswers.id">
                    <option value="" key="0" />
                    {formAnswers
                      ? formAnswers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/answer" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  formQuestions: storeState.formQuestion.entities,
  formAnswers: storeState.formAnswers.entities,
  answerEntity: storeState.answer.entity,
  loading: storeState.answer.loading,
  updating: storeState.answer.updating,
  updateSuccess: storeState.answer.updateSuccess
});

const mapDispatchToProps = {
  getFormQuestions,
  getFormAnswers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AnswerUpdate);
