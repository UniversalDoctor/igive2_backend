import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStudy } from 'app/shared/model/study.model';
import { getEntities as getStudies } from 'app/entities/study/study.reducer';
import { getEntity, updateEntity, createEntity, reset } from './participant-invitation.reducer';
import { IParticipantInvitation } from 'app/shared/model/participant-invitation.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IParticipantInvitationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IParticipantInvitationUpdateState {
  isNew: boolean;
  studyId: string;
}

export class ParticipantInvitationUpdate extends React.Component<IParticipantInvitationUpdateProps, IParticipantInvitationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      studyId: '0',
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

    this.props.getStudies();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { participantInvitationEntity } = this.props;
      const entity = {
        ...participantInvitationEntity,
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
    this.props.history.push('/participant-invitation');
  };

  render() {
    const { participantInvitationEntity, studies, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.participantInvitation.home.createOrEditLabel">
              <Translate contentKey="igive2App.participantInvitation.home.createOrEditLabel">
                Create or edit a ParticipantInvitation
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : participantInvitationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="participant-invitation-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="participant-invitation-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="emailLabel" for="participant-invitation-email">
                    <Translate contentKey="igive2App.participantInvitation.email">Email</Translate>
                  </Label>
                  <AvField
                    id="participant-invitation-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel" check>
                    <AvInput id="participant-invitation-state" type="checkbox" className="form-control" name="state" />
                    <Translate contentKey="igive2App.participantInvitation.state">State</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="participantIdLabel" for="participant-invitation-participantId">
                    <Translate contentKey="igive2App.participantInvitation.participantId">Participant Id</Translate>
                  </Label>
                  <AvField id="participant-invitation-participantId" type="text" name="participantId" />
                </AvGroup>
                <AvGroup>
                  <Label for="participant-invitation-study">
                    <Translate contentKey="igive2App.participantInvitation.study">Study</Translate>
                  </Label>
                  <AvInput id="participant-invitation-study" type="select" className="form-control" name="study.id">
                    <option value="" key="0" />
                    {studies
                      ? studies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/participant-invitation" replace color="info">
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
  studies: storeState.study.entities,
  participantInvitationEntity: storeState.participantInvitation.entity,
  loading: storeState.participantInvitation.loading,
  updating: storeState.participantInvitation.updating,
  updateSuccess: storeState.participantInvitation.updateSuccess
});

const mapDispatchToProps = {
  getStudies,
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
)(ParticipantInvitationUpdate);
