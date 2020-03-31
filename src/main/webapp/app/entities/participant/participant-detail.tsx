import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './participant.reducer';
import { IParticipant } from 'app/shared/model/participant.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IParticipantDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ParticipantDetail extends React.Component<IParticipantDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { participantEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.participant.detail.title">Participant</Translate> [<b>{participantEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="entryDate">
                <Translate contentKey="igive2App.participant.entryDate">Entry Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={participantEntity.entryDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="anonymousId">
                <Translate contentKey="igive2App.participant.anonymousId">Anonymous Id</Translate>
              </span>
            </dt>
            <dd>{participantEntity.anonymousId}</dd>
            <dt>
              <Translate contentKey="igive2App.participant.mobileUser">Mobile User</Translate>
            </dt>
            <dd>{participantEntity.mobileUser ? participantEntity.mobileUser.id : ''}</dd>
            <dt>
              <Translate contentKey="igive2App.participant.study">Study</Translate>
            </dt>
            <dd>{participantEntity.study ? participantEntity.study.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/participant" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/participant/${participantEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ participant }: IRootState) => ({
  participantEntity: participant.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ParticipantDetail);
