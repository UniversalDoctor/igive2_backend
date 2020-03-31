import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { getEntities as getMobileUsers } from 'app/entities/mobile-user/mobile-user.reducer';
import { IStudy } from 'app/shared/model/study.model';
import { getEntities as getStudies } from 'app/entities/study/study.reducer';
import { getEntity, updateEntity, createEntity, reset } from './participant.reducer';
import { IParticipant } from 'app/shared/model/participant.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IParticipantUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IParticipantUpdateState {
  isNew: boolean;
  mobileUserId: string;
  studyId: string;
}

export class ParticipantUpdate extends React.Component<IParticipantUpdateProps, IParticipantUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      mobileUserId: '0',
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

    this.props.getMobileUsers();
    this.props.getStudies();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { participantEntity } = this.props;
      const entity = {
        ...participantEntity,
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
    this.props.history.push('/participant');
  };

  render() {
    const { participantEntity, mobileUsers, studies, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.participant.home.createOrEditLabel">
              <Translate contentKey="igive2App.participant.home.createOrEditLabel">Create or edit a Participant</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : participantEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="participant-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="participant-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="entryDateLabel" for="participant-entryDate">
                    <Translate contentKey="igive2App.participant.entryDate">Entry Date</Translate>
                  </Label>
                  <AvField
                    id="participant-entryDate"
                    type="date"
                    className="form-control"
                    name="entryDate"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="anonymousIdLabel" for="participant-anonymousId">
                    <Translate contentKey="igive2App.participant.anonymousId">Anonymous Id</Translate>
                  </Label>
                  <AvField
                    id="participant-anonymousId"
                    type="text"
                    name="anonymousId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="participant-mobileUser">
                    <Translate contentKey="igive2App.participant.mobileUser">Mobile User</Translate>
                  </Label>
                  <AvInput id="participant-mobileUser" type="select" className="form-control" name="mobileUser.id">
                    <option value="" key="0" />
                    {mobileUsers
                      ? mobileUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="participant-study">
                    <Translate contentKey="igive2App.participant.study">Study</Translate>
                  </Label>
                  <AvInput id="participant-study" type="select" className="form-control" name="study.id">
                    <option value="" key="0" />
                    {studies
                      ? studies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/participant" replace color="info">
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
  mobileUsers: storeState.mobileUser.entities,
  studies: storeState.study.entities,
  participantEntity: storeState.participant.entity,
  loading: storeState.participant.loading,
  updating: storeState.participant.updating,
  updateSuccess: storeState.participant.updateSuccess
});

const mapDispatchToProps = {
  getMobileUsers,
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
)(ParticipantUpdate);
