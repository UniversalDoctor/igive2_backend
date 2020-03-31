import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './answer.reducer';
import { IAnswer } from 'app/shared/model/answer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnswerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AnswerDetail extends React.Component<IAnswerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { answerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.answer.detail.title">Answer</Translate> [<b>{answerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="data">
                <Translate contentKey="igive2App.answer.data">Data</Translate>
              </span>
            </dt>
            <dd>{answerEntity.data}</dd>
            <dt>
              <Translate contentKey="igive2App.answer.formQuestion">Form Question</Translate>
            </dt>
            <dd>{answerEntity.formQuestion ? answerEntity.formQuestion.id : ''}</dd>
            <dt>
              <Translate contentKey="igive2App.answer.formAnswers">Form Answers</Translate>
            </dt>
            <dd>{answerEntity.formAnswers ? answerEntity.formAnswers.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/answer" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/answer/${answerEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ answer }: IRootState) => ({
  answerEntity: answer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AnswerDetail);
