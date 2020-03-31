import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAnswer } from 'app/shared/model/answer.model';
import { getEntities as getAnswers } from 'app/entities/answer/answer.reducer';
import { IForm } from 'app/shared/model/form.model';
import { getEntities as getForms } from 'app/entities/form/form.reducer';
import { getEntity, updateEntity, createEntity, reset } from './form-question.reducer';
import { IFormQuestion } from 'app/shared/model/form-question.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFormQuestionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFormQuestionUpdateState {
  isNew: boolean;
  answerId: string;
  formId: string;
}

export class FormQuestionUpdate extends React.Component<IFormQuestionUpdateProps, IFormQuestionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      answerId: '0',
      formId: '0',
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

    this.props.getAnswers();
    this.props.getForms();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { formQuestionEntity } = this.props;
      const entity = {
        ...formQuestionEntity,
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
    this.props.history.push('/form-question');
  };

  render() {
    const { formQuestionEntity, answers, forms, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="igive2App.formQuestion.home.createOrEditLabel">
              <Translate contentKey="igive2App.formQuestion.home.createOrEditLabel">Create or edit a FormQuestion</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : formQuestionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="form-question-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="form-question-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="questionLabel" for="form-question-question">
                    <Translate contentKey="igive2App.formQuestion.question">Question</Translate>
                  </Label>
                  <AvField
                    id="form-question-question"
                    type="text"
                    name="question"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="isMandatoryLabel" check>
                    <AvInput id="form-question-isMandatory" type="checkbox" className="form-control" name="isMandatory" />
                    <Translate contentKey="igive2App.formQuestion.isMandatory">Is Mandatory</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="form-question-type">
                    <Translate contentKey="igive2App.formQuestion.type">Type</Translate>
                  </Label>
                  <AvInput
                    id="form-question-type"
                    type="select"
                    className="form-control"
                    name="type"
                    value={(!isNew && formQuestionEntity.type) || 'FREEANSWER'}
                  >
                    <option value="FREEANSWER">{translate('igive2App.QuestionType.FREEANSWER')}</option>
                    <option value="FREELONGANSWER">{translate('igive2App.QuestionType.FREELONGANSWER')}</option>
                    <option value="SINGLECHECKBOX">{translate('igive2App.QuestionType.SINGLECHECKBOX')}</option>
                    <option value="MULTIPLECHECKBOX">{translate('igive2App.QuestionType.MULTIPLECHECKBOX')}</option>
                    <option value="DATEANSWER">{translate('igive2App.QuestionType.DATEANSWER')}</option>
                    <option value="NUMERICANSWER">{translate('igive2App.QuestionType.NUMERICANSWER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="optionsLabel" for="form-question-options">
                    <Translate contentKey="igive2App.formQuestion.options">Options</Translate>
                  </Label>
                  <AvField id="form-question-options" type="text" name="options" />
                </AvGroup>
                <AvGroup>
                  <Label for="form-question-form">
                    <Translate contentKey="igive2App.formQuestion.form">Form</Translate>
                  </Label>
                  <AvInput id="form-question-form" type="select" className="form-control" name="form.id">
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
                <Button tag={Link} id="cancel-save" to="/form-question" replace color="info">
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
  answers: storeState.answer.entities,
  forms: storeState.form.entities,
  formQuestionEntity: storeState.formQuestion.entity,
  loading: storeState.formQuestion.loading,
  updating: storeState.formQuestion.updating,
  updateSuccess: storeState.formQuestion.updateSuccess
});

const mapDispatchToProps = {
  getAnswers,
  getForms,
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
)(FormQuestionUpdate);
