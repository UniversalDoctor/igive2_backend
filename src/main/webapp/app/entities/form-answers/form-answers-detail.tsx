import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './form-answers.reducer';
import { IFormAnswers } from 'app/shared/model/form-answers.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFormAnswersDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FormAnswersDetail extends React.Component<IFormAnswersDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { formAnswersEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.formAnswers.detail.title">FormAnswers</Translate> [<b>{formAnswersEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="savedDate">
                <Translate contentKey="igive2App.formAnswers.savedDate">Saved Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={formAnswersEntity.savedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="completed">
                <Translate contentKey="igive2App.formAnswers.completed">Completed</Translate>
              </span>
            </dt>
            <dd>{formAnswersEntity.completed ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="igive2App.formAnswers.form">Form</Translate>
            </dt>
            <dd>{formAnswersEntity.form ? formAnswersEntity.form.id : ''}</dd>
            <dt>
              <Translate contentKey="igive2App.formAnswers.participant">Participant</Translate>
            </dt>
            <dd>{formAnswersEntity.participant ? formAnswersEntity.participant.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/form-answers" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/form-answers/${formAnswersEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ formAnswers }: IRootState) => ({
  formAnswersEntity: formAnswers.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FormAnswersDetail);
