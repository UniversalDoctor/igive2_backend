import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFormAnswers } from 'app/shared/model/form-answers.model';
import { getEntities as getFormAnswers } from 'app/entities/form-answers/form-answers.reducer';
import { IStudy } from 'app/shared/model/study.model';
import { getEntities as getStudies } from 'app/entities/study/study.reducer';
import { getEntity, updateEntity, createEntity, reset } from './form.reducer';
import { IForm } from 'app/shared/model/form.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFormUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFormUpdateState {
  isNew: boolean;
  formAnswersId: string;
  studyId: string;
}

export class FormUpdate extends React.Component<IFormUpdateProps, IFormUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      formAnswersId: '0',
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

    this.props.getFormAnswers();
    this.props.getStudies();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { formEntity } = this.props;
      const entity = {
        ...formEntity,
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
    this.props.history.push('/form');
  };

  render() {
    const { formEntity, formAnswers, studies, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.form.home.createOrEditLabel">
              <Translate contentKey="igive2App.form.home.createOrEditLabel">Create or edit a Form</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : formEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="form-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="form-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="form-name">
                    <Translate contentKey="igive2App.form.name">Name</Translate>
                  </Label>
                  <AvField
                    id="form-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 100, errorMessage: translate('entity.validation.maxlength', { max: 100 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="form-description">
                    <Translate contentKey="igive2App.form.description">Description</Translate>
                  </Label>
                  <AvField
                    id="form-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 500, errorMessage: translate('entity.validation.maxlength', { max: 500 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel" for="form-state">
                    <Translate contentKey="igive2App.form.state">State</Translate>
                  </Label>
                  <AvInput
                    id="form-state"
                    type="select"
                    className="form-control"
                    name="state"
                    value={(!isNew && formEntity.state) || 'DRAFT'}
                  >
                    <option value="DRAFT">{translate('igive2App.State.DRAFT')}</option>
                    <option value="PUBLISHED">{translate('igive2App.State.PUBLISHED')}</option>
                    <option value="FINISHED">{translate('igive2App.State.FINISHED')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="form-study">
                    <Translate contentKey="igive2App.form.study">Study</Translate>
                  </Label>
                  <AvInput id="form-study" type="select" className="form-control" name="study.id">
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
                <Button tag={Link} id="cancel-save" to="/form" replace color="info">
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
  formAnswers: storeState.formAnswers.entities,
  studies: storeState.study.entities,
  formEntity: storeState.form.entity,
  loading: storeState.form.loading,
  updating: storeState.form.updating,
  updateSuccess: storeState.form.updateSuccess
});

const mapDispatchToProps = {
  getFormAnswers,
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
)(FormUpdate);
