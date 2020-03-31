// modificado
/*
import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import {IUser} from 'app/shared/model/user.model'
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { getEntities as getMobileUsers } from 'app/entities/mobile-user/mobile-user.reducer';
import { IResearcher } from 'app/shared/model/researcher.model';
import { getEntities as getResearchers } from 'app/entities/researcher/researcher.reducer';
import { getEntity, updateEntity, createEntity, reset } from './i-give-2-user.reducer';
import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IIGive2UserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IIGive2UserUpdateState {
  isNew: boolean;
  mobileUserId: string;
  researcherId: string;
  userId;
}

export class IGive2UserUpdate extends React.Component<IIGive2UserUpdateProps, IIGive2UserUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      mobileUserId: '0',
      researcherId: '0',
      userId:'0',
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
    this.props.getResearchers();
    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { iGive2UserEntity } = this.props;
      const entity = {
        ...iGive2UserEntity,
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
    this.props.history.push('/i-give-2-user');
  };

  render() {
    const { iGive2UserEntity, mobileUsers, researchers, user,loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.iGive2User.home.createOrEditLabel">
              <Translate contentKey="igive2App.iGive2User.home.createOrEditLabel">Create or edit a IGive2User</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : iGive2UserEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="i-give-2-user-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="i-give-2-user-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="newsletterLabel" check>
                    <AvInput id="i-give-2-user-newsletter" type="checkbox" className="form-control" name="newsletter" />
                    <Translate contentKey="igive2App.iGive2User.newsletter">Newsletter</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="termsAcceptedLabel" check>
                    <AvInput id="i-give-2-user-termsAccepted" type="checkbox" className="form-control" name="termsAccepted" />
                    <Translate contentKey="igive2App.iGive2User.termsAccepted">Terms Accepted</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="countryLabel" for="i-give-2-user-country">
                    <Translate contentKey="igive2App.iGive2User.country">Country</Translate>
                  </Label>
                  <AvField
                    id="i-give-2-user-country"
                    type="text"
                    name="country"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                    </AvGroup>
                    <AvGroup>
                      <Label for="iGive2User-user">
                        <Translate contentKey="igive2App.iGive2User.user">User</Translate>
                      </Label>
                      <AvInput id="iGive2User-user" type="select" className="form-control" name="user.id">
                        <option value="" key="0" />
                        {users
                          ? users.map(otherEntity => (
                              <option value={otherEntity.id} key={otherEntity.id}>
                                {otherEntity.id}
                              </option>
                            ))
                          : null}
                      </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/i-give-2-user" replace color="info">
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
  researchers: storeState.researcher.entities,
  users:storeState.user.entities,
  iGive2UserEntity: storeState.iGive2User.entity,
  loading: storeState.iGive2User.loading,
  updating: storeState.iGive2User.updating,
  updateSuccess: storeState.iGive2User.updateSuccess
});

const mapDispatchToProps = {
  getMobileUsers,
  getResearchers,
  getUsers,
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
)(IGive2UserUpdate);
*/
/** *************original************************ **/
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
import { IResearcher } from 'app/shared/model/researcher.model';
import { getEntities as getResearchers } from 'app/entities/researcher/researcher.reducer';
import { getEntity, updateEntity, createEntity, reset } from './i-give-2-user.reducer';
import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IIGive2UserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IIGive2UserUpdateState {
  isNew: boolean;
  mobileUserId: string;
  researcherId: string;
}

export class IGive2UserUpdate extends React.Component<IIGive2UserUpdateProps, IIGive2UserUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      mobileUserId: '0',
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

    this.props.getMobileUsers();
    this.props.getResearchers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { iGive2UserEntity } = this.props;
      const entity = {
        ...iGive2UserEntity,
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
    this.props.history.push('/i-give-2-user');
  };

  render() {
    const { iGive2UserEntity, mobileUsers, researchers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.iGive2User.home.createOrEditLabel">
              <Translate contentKey="igive2App.iGive2User.home.createOrEditLabel">Create or edit a IGive2User</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : iGive2UserEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="i-give-2-user-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="i-give-2-user-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="newsletterLabel" check>
                    <AvInput id="i-give-2-user-newsletter" type="checkbox" className="form-control" name="newsletter" />
                    <Translate contentKey="igive2App.iGive2User.newsletter">Newsletter</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="termsAcceptedLabel" check>
                    <AvInput id="i-give-2-user-termsAccepted" type="checkbox" className="form-control" name="termsAccepted" />
                    <Translate contentKey="igive2App.iGive2User.termsAccepted">Terms Accepted</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="countryLabel" for="i-give-2-user-country">
                    <Translate contentKey="igive2App.iGive2User.country">Country</Translate>
                  </Label>
                  <AvField
                    id="i-give-2-user-country"
                    type="text"
                    name="country"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/i-give-2-user" replace color="info">
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
  researchers: storeState.researcher.entities,
  iGive2UserEntity: storeState.iGive2User.entity,
  loading: storeState.iGive2User.loading,
  updating: storeState.iGive2User.updating,
  updateSuccess: storeState.iGive2User.updateSuccess
});

const mapDispatchToProps = {
  getMobileUsers,
  getResearchers,
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
)(IGive2UserUpdate);

