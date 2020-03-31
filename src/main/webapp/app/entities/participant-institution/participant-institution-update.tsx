import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStudy } from 'app/shared/model/study.model';
import { getEntities as getStudies } from 'app/entities/study/study.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './participant-institution.reducer';
import { IParticipantInstitution } from 'app/shared/model/participant-institution.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IParticipantInstitutionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IParticipantInstitutionUpdateState {
  isNew: boolean;
  studyId: string;
}

export class ParticipantInstitutionUpdate extends React.Component<IParticipantInstitutionUpdateProps, IParticipantInstitutionUpdateState> {
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { participantInstitutionEntity } = this.props;
      const entity = {
        ...participantInstitutionEntity,
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
    this.props.history.push('/participant-institution');
  };

  render() {
    const { participantInstitutionEntity, studies, loading, updating } = this.props;
    const { isNew } = this.state;

    const { logo, logoContentType } = participantInstitutionEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.participantInstitution.home.createOrEditLabel">
              <Translate contentKey="igive2App.participantInstitution.home.createOrEditLabel">
                Create or edit a ParticipantInstitution
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : participantInstitutionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="participant-institution-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="participant-institution-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <AvGroup>
                    <Label id="logoLabel" for="logo">
                      <Translate contentKey="igive2App.participantInstitution.logo">Logo</Translate>
                    </Label>
                    <br />
                    {logo ? (
                      <div>
                        <a onClick={openFile(logoContentType, logo)}>
                          <img src={`data:${logoContentType};base64,${logo}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {logoContentType}, {byteSize(logo)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('logo')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_logo" type="file" onChange={this.onBlobChange(true, 'logo')} accept="image/*" />
                    <AvInput type="hidden" name="logo" value={logo} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label for="participant-institution-study">
                    <Translate contentKey="igive2App.participantInstitution.study">Study</Translate>
                  </Label>
                  <AvInput id="participant-institution-study" type="select" className="form-control" name="study.id">
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
                <Button tag={Link} id="cancel-save" to="/participant-institution" replace color="info">
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
  participantInstitutionEntity: storeState.participantInstitution.entity,
  loading: storeState.participantInstitution.loading,
  updating: storeState.participantInstitution.updating,
  updateSuccess: storeState.participantInstitution.updateSuccess
});

const mapDispatchToProps = {
  getStudies,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ParticipantInstitutionUpdate);
