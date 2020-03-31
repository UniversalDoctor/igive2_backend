import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IForm } from 'app/shared/model/form.model';
import { getEntities as getForms } from 'app/entities/form/form.reducer';
import { IParticipant } from 'app/shared/model/participant.model';
import { getEntities as getParticipants } from 'app/entities/participant/participant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './form-answers.reducer';
import { IFormAnswers } from 'app/shared/model/form-answers.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFormAnswersUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFormAnswersUpdateState {
  isNew: boolean;
  formId: string;
  participantId: string;
}

export class FormAnswersUpdate extends React.Component<IFormAnswersUpdateProps, IFormAnswersUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      formId: '0',
      participantId: '0',
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

    this.props.getForms();
    this.props.getParticipants();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { formAnswersEntity } = this.props;
      const entity = {
        ...formAnswersEntity,
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
    this.props.history.push('/form-answers');
  };

  render() {
    const { formAnswersEntity, forms, participants, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.formAnswers.home.createOrEditLabel">
              <Translate contentKey="igive2App.formAnswers.home.createOrEditLabel">Create or edit a FormAnswers</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : formAnswersEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="form-answers-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="form-answers-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="savedDateLabel" for="form-answers-savedDate">
                    <Translate contentKey="igive2App.formAnswers.savedDate">Saved Date</Translate>
                  </Label>
                  <AvField id="form-answers-savedDate" type="date" className="form-control" name="savedDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="completedLabel" check>
                    <AvInput id="form-answers-completed" type="checkbox" className="form-control" name="completed" />
                    <Translate contentKey="igive2App.formAnswers.completed">Completed</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="form-answers-form">
                    <Translate contentKey="igive2App.formAnswers.form">Form</Translate>
                  </Label>
                  <AvInput id="form-answers-form" type="select" className="form-control" name="form.id">
                    <option value="" key="0" />
                    {forms
                      ? forms.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="form-answers-participant">
                    <Translate contentKey="igive2App.formAnswers.participant">Participant</Translate>
                  </Label>
                  <AvInput id="form-answers-participant" type="select" className="form-control" name="participant.id">
                    <option value="" key="0" />
                    {participants
                      ? participants.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/form-answers" replace color="info">
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
  forms: storeState.form.entities,
  participants: storeState.participant.entities,
  formAnswersEntity: storeState.formAnswers.entity,
  loading: storeState.formAnswers.loading,
  updating: storeState.formAnswers.updating,
  updateSuccess: storeState.formAnswers.updateSuccess
});

const mapDispatchToProps = {
  getForms,
  getParticipants,
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
)(FormAnswersUpdate);
