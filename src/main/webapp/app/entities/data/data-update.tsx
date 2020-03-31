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
import { IParticipant } from 'app/shared/model/participant.model';
import { getEntities as getParticipants } from 'app/entities/participant/participant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './data.reducer';
import { IData } from 'app/shared/model/data.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDataUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDataUpdateState {
  isNew: boolean;
  mobileUserId: string;
  participantId: string;
}

export class DataUpdate extends React.Component<IDataUpdateProps, IDataUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      mobileUserId: '0',
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

    this.props.getMobileUsers();
    this.props.getParticipants();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { dataEntity } = this.props;
      const entity = {
        ...dataEntity,
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
    this.props.history.push('/data');
  };

  render() {
    const { dataEntity, mobileUsers, participants, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.data.home.createOrEditLabel">
              <Translate contentKey="igive2App.data.home.createOrEditLabel">Create or edit a Data</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : dataEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="data-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="data-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dataLabel" for="data-data">
                    <Translate contentKey="igive2App.data.data">Data</Translate>
                  </Label>
                  <AvInput
                    id="data-data"
                    type="select"
                    className="form-control"
                    name="data"
                    value={(!isNew && dataEntity.data) || 'HEIGHT'}
                  >
                    <option value="HEIGHT">{translate('igive2App.DataType.HEIGHT')}</option>
                    <option value="WEIGHT">{translate('igive2App.DataType.WEIGHT')}</option>
                    <option value="STEPS">{translate('igive2App.DataType.STEPS')}</option>
                    <option value="ACTIVETIME">{translate('igive2App.DataType.ACTIVETIME')}</option>
                    <option value="SEATEDTIME">{translate('igive2App.DataType.SEATEDTIME')}</option>
                    <option value="SYSTOLIC">{translate('igive2App.DataType.SYSTOLIC')}</option>
                    <option value="DYASTOLIC">{translate('igive2App.DataType.DYASTOLIC')}</option>
                    <option value="BREATHSPERMINUTE">{translate('igive2App.DataType.BREATHSPERMINUTE')}</option>
                    <option value="BREATHINGRAWDATA">{translate('igive2App.DataType.BREATHINGRAWDATA')}</option>
                    <option value="SLEEP">{translate('igive2App.DataType.SLEEP')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="notesLabel" for="data-notes">
                    <Translate contentKey="igive2App.data.notes">Notes</Translate>
                  </Label>
                  <AvField id="data-notes" type="text" name="notes" />
                </AvGroup>
                <AvGroup>
                  <Label id="dateLabel" for="data-date">
                    <Translate contentKey="igive2App.data.date">Date</Translate>
                  </Label>
                  <AvField id="data-date" type="date" className="form-control" name="date" />
                </AvGroup>
                <AvGroup>
                  <Label id="valueLabel" for="data-value">
                    <Translate contentKey="igive2App.data.value">Value</Translate>
                  </Label>
                  <AvField
                    id="data-value"
                    type="text"
                    name="value"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="data-mobileUser">
                    <Translate contentKey="igive2App.data.mobileUser">Mobile User</Translate>
                  </Label>
                  <AvInput id="data-mobileUser" type="select" className="form-control" name="mobileUser.id">
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
                  <Label for="data-participant">
                    <Translate contentKey="igive2App.data.participant">Participant</Translate>
                  </Label>
                  <AvInput id="data-participant" type="select" className="form-control" name="participant.id">
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
                <Button tag={Link} id="cancel-save" to="/data" replace color="info">
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
  participants: storeState.participant.entities,
  dataEntity: storeState.data.entity,
  loading: storeState.data.loading,
  updating: storeState.data.updating,
  updateSuccess: storeState.data.updateSuccess
});

const mapDispatchToProps = {
  getMobileUsers,
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
)(DataUpdate);
