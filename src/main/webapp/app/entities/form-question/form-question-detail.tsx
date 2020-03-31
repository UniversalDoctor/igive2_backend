import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './form-question.reducer';
import { IFormQuestion } from 'app/shared/model/form-question.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFormQuestionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FormQuestionDetail extends React.Component<IFormQuestionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { formQuestionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.formQuestion.detail.title">FormQuestion</Translate> [<b>{formQuestionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="question">
                <Translate contentKey="igive2App.formQuestion.question">Question</Translate>
              </span>
            </dt>
            <dd>{formQuestionEntity.question}</dd>
            <dt>
              <span id="isMandatory">
                <Translate contentKey="igive2App.formQuestion.isMandatory">Is Mandatory</Translate>
              </span>
            </dt>
            <dd>{formQuestionEntity.isMandatory ? 'true' : 'false'}</dd>
            <dt>
              <span id="type">
                <Translate contentKey="igive2App.formQuestion.type">Type</Translate>
              </span>
            </dt>
            <dd>{formQuestionEntity.type}</dd>
            <dt>
              <span id="options">
                <Translate contentKey="igive2App.formQuestion.options">Options</Translate>
              </span>
            </dt>
            <dd>{formQuestionEntity.options}</dd>
            <dt>
              <Translate contentKey="igive2App.formQuestion.form">Form</Translate>
            </dt>
            <dd>{formQuestionEntity.form ? formQuestionEntity.form.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/form-question" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/form-question/${formQuestionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ formQuestion }: IRootState) => ({
  formQuestionEntity: formQuestion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FormQuestionDetail);
