import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { getEntities as getIGive2Users } from 'app/entities/i-give-2-user/i-give-2-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './researcher.reducer';
import { IResearcher } from 'app/shared/model/researcher.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IResearcherUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IResearcherUpdateState {
  isNew: boolean;
  iGive2UserId: string;
}

export class ResearcherUpdate extends React.Component<IResearcherUpdateProps, IResearcherUpdateState> {
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

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { researcherEntity } = this.props;
      const entity = {
        ...researcherEntity,
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
    this.props.history.push('/researcher');
  };

  render() {
    const { researcherEntity, iGive2Users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.researcher.home.createOrEditLabel">
              <Translate contentKey="igive2App.researcher.home.createOrEditLabel">Create or edit a Researcher</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : researcherEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="researcher-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="researcher-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="institutionLabel" for="researcher-institution">
                    <Translate contentKey="igive2App.researcher.institution">Institution</Translate>
                  </Label>
                  <AvField
                    id="researcher-institution"
                    type="text"
                    name="institution"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="honorificsLabel" for="researcher-honorifics">
                    <Translate contentKey="igive2App.researcher.honorifics">Honorifics</Translate>
                  </Label>
                  <AvField
                    id="researcher-honorifics"
                    type="text"
                    name="honorifics"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="userIdLabel" for="researcher-userId">
                    <Translate contentKey="igive2App.researcher.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="researcher-userId"
                    type="text"
                    name="userId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="researcher-iGive2User">
                    <Translate contentKey="igive2App.researcher.iGive2User">I Give 2 User</Translate>
                  </Label>
                  <AvInput id="researcher-iGive2User" type="select" className="form-control" name="iGive2User.id">
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
                <Button tag={Link} id="cancel-save" to="/researcher" replace color="info">
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
  researcherEntity: storeState.researcher.entity,
  loading: storeState.researcher.loading,
  updating: storeState.researcher.updating,
  updateSuccess: storeState.researcher.updateSuccess
});

const mapDispatchToProps = {
  getIGive2Users,
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
)(ResearcherUpdate);
