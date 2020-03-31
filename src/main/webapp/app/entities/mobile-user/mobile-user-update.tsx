import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { getEntities as getIGive2Users } from 'app/entities/i-give-2-user/i-give-2-user.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './mobile-user.reducer';
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMobileUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMobileUserUpdateState {
  isNew: boolean;
  iGive2UserId: string;
}

export class MobileUserUpdate extends React.Component<IMobileUserUpdateProps, IMobileUserUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      iGive2UserId: '0',
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

    this.props.getIGive2Users();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { mobileUserEntity } = this.props;
      const entity = {
        ...mobileUserEntity,
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
    this.props.history.push('/mobile-user');
  };

  render() {
    const { mobileUserEntity, iGive2Users, loading, updating } = this.props;
    const { isNew } = this.state;

    const { icon, iconContentType } = mobileUserEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.mobileUser.home.createOrEditLabel">
              <Translate contentKey="igive2App.mobileUser.home.createOrEditLabel">Create or edit a MobileUser</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : mobileUserEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="mobile-user-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="mobile-user-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="genderLabel" for="mobile-user-gender">
                    <Translate contentKey="igive2App.mobileUser.gender">Gender</Translate>
                  </Label>
                  <AvInput
                    id="mobile-user-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && mobileUserEntity.gender) || 'FEMALE'}
                  >
                    <option value="FEMALE">{translate('igive2App.GenderType.FEMALE')}</option>
                    <option value="MALE">{translate('igive2App.GenderType.MALE')}</option>
                    <option value="OTHER">{translate('igive2App.GenderType.OTHER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="birthdateLabel" for="mobile-user-birthdate">
                    <Translate contentKey="igive2App.mobileUser.birthdate">Birthdate</Translate>
                  </Label>
                  <AvField id="mobile-user-birthdate" type="date" className="form-control" name="birthdate" />
                </AvGroup>
                <AvGroup>
                  <Label id="diseasesLabel" for="mobile-user-diseases">
                    <Translate contentKey="igive2App.mobileUser.diseases">Diseases</Translate>
                  </Label>
                  <AvInput
                    id="mobile-user-diseases"
                    type="select"
                    className="form-control"
                    name="diseases"
                    value={(!isNew && mobileUserEntity.diseases) || 'HYPERTENSION'}
                  >
                    <option value="HYPERTENSION">{translate('igive2App.Diseases.HYPERTENSION')}</option>
                    <option value="NONE">{translate('igive2App.Diseases.NONE')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="userIdLabel" for="mobile-user-userId">
                    <Translate contentKey="igive2App.mobileUser.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="mobile-user-userId"
                    type="text"
                    name="userId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="mobile-user-status">
                    <Translate contentKey="igive2App.mobileUser.status">Status</Translate>
                  </Label>
                  <AvField id="mobile-user-status" type="text" name="status" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="iconLabel" for="icon">
                      <Translate contentKey="igive2App.mobileUser.icon">Icon</Translate>
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
                  <Label id="usernameLabel" for="mobile-user-username">
                    <Translate contentKey="igive2App.mobileUser.username">Username</Translate>
                  </Label>
                  <AvField id="mobile-user-username" type="text" name="username" />
                </AvGroup>
                <AvGroup>
                  <Label for="mobile-user-iGive2User">
                    <Translate contentKey="igive2App.mobileUser.iGive2User">I Give 2 User</Translate>
                  </Label>
                  <AvInput id="mobile-user-iGive2User" type="select" className="form-control" name="iGive2User.id">
                    <option value="" key="0" />
                    {iGive2Users
                      ? iGive2Users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/mobile-user" replace color="info">
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
  iGive2Users: storeState.iGive2User.entities,
  mobileUserEntity: storeState.mobileUser.entity,
  loading: storeState.mobileUser.loading,
  updating: storeState.mobileUser.updating,
  updateSuccess: storeState.mobileUser.updateSuccess
});

const mapDispatchToProps = {
  getIGive2Users,
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
)(MobileUserUpdate);
