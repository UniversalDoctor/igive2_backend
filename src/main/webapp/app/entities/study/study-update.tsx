import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IResearcher } from 'app/shared/model/researcher.model';
import { getEntities as getResearchers } from 'app/entities/researcher/researcher.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './study.reducer';
import { IStudy } from 'app/shared/model/study.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStudyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IStudyUpdateState {
  isNew: boolean;
  researcherId: string;
}

export class StudyUpdate extends React.Component<IStudyUpdateProps, IStudyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      researcherId: '0',
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

    this.props.getResearchers();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { studyEntity } = this.props;
      const entity = {
        ...studyEntity,
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
    this.props.history.push('/study');
  };

  render() {
    const { studyEntity, researchers, loading, updating } = this.props;
    const { isNew } = this.state;

    const { icon, iconContentType } = studyEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.study.home.createOrEditLabel">
              <Translate contentKey="igive2App.study.home.createOrEditLabel">Create or edit a Study</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : studyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="study-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="study-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="codeLabel" for="study-code">
                    <Translate contentKey="igive2App.study.code">Code</Translate>
                  </Label>
                  <AvField
                    id="study-code"
                    type="text"
                    name="code"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 100, errorMessage: translate('entity.validation.maxlength', { max: 100 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="iconLabel" for="icon">
                      <Translate contentKey="igive2App.study.icon">Icon</Translate>
                    </Label>
                    <br />
                    {icon ? (
                      <div>
                        <a onClick={openFile(iconContentType, icon)}>
                          <img src={`data:${iconContentType};base64,${icon}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {iconContentType}, {byteSize(icon)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('icon')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_icon" type="file" onChange={this.onBlobChange(true, 'icon')} accept="image/*" />
                    <AvInput type="hidden" name="icon" value={icon} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="study-name">
                    <Translate contentKey="igive2App.study.name">Name</Translate>
                  </Label>
                  <AvField
                    id="study-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 100, errorMessage: translate('entity.validation.maxlength', { max: 100 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="study-description">
                    <Translate contentKey="igive2App.study.description">Description</Translate>
                  </Label>
                  <AvField
                    id="study-description"
                    type="text"
                    name="description"
                    validate={{
                      maxLength: { value: 1000, errorMessage: translate('entity.validation.maxlength', { max: 1000 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="moreInfoLabel" for="study-moreInfo">
                    <Translate contentKey="igive2App.study.moreInfo">More Info</Translate>
                  </Label>
                  <AvField id="study-moreInfo" type="text" name="moreInfo" />
                </AvGroup>
                <AvGroup>
                  <Label id="contactEmailLabel" for="study-contactEmail">
                    <Translate contentKey="igive2App.study.contactEmail">Contact Email</Translate>
                  </Label>
                  <AvField id="study-contactEmail" type="text" name="contactEmail" />
                </AvGroup>
                <AvGroup>
                  <Label id="startDateLabel" for="study-startDate">
                    <Translate contentKey="igive2App.study.startDate">Start Date</Translate>
                  </Label>
                  <AvField id="study-startDate" type="date" className="form-control" name="startDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="study-endDate">
                    <Translate contentKey="igive2App.study.endDate">End Date</Translate>
                  </Label>
                  <AvField id="study-endDate" type="date" className="form-control" name="endDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel" for="study-state">
                    <Translate contentKey="igive2App.study.state">State</Translate>
                  </Label>
                  <AvInput
                    id="study-state"
                    type="select"
                    className="form-control"
                    name="state"
                    value={(!isNew && studyEntity.state) || 'DRAFT'}
                  >
                    <option value="DRAFT">{translate('igive2App.State.DRAFT')}</option>
                    <option value="PUBLISHED">{translate('igive2App.State.PUBLISHED')}</option>
                    <option value="FINISHED">{translate('igive2App.State.FINISHED')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="recruitingLabel" check>
                    <AvInput id="study-recruiting" type="checkbox" className="form-control" name="recruiting" />
                    <Translate contentKey="igive2App.study.recruiting">Recruiting</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="requestedDataLabel" for="study-requestedData">
                    <Translate contentKey="igive2App.study.requestedData">Requested Data</Translate>
                  </Label>
                  <AvField id="study-requestedData" type="text" name="requestedData" />
                </AvGroup>
                <AvGroup>
                  <Label id="dataJustificationLabel" for="study-dataJustification">
                    <Translate contentKey="igive2App.study.dataJustification">Data Justification</Translate>
                  </Label>
                  <AvField id="study-dataJustification" type="text" name="dataJustification" />
                </AvGroup>
                <AvGroup>
                  <Label for="study-researcher">
                    <Translate contentKey="igive2App.study.researcher">Researcher</Translate>
                  </Label>
                  <AvInput id="study-researcher" type="select" className="form-control" name="researcher.id">
                    <option value="" key="0" />
                    {researchers
                      ? researchers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/study" replace color="info">
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
  researchers: storeState.researcher.entities,
  studyEntity: storeState.study.entity,
  loading: storeState.study.loading,
  updating: storeState.study.updating,
  updateSuccess: storeState.study.updateSuccess
});

const mapDispatchToProps = {
  getResearchers,
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
)(StudyUpdate);
