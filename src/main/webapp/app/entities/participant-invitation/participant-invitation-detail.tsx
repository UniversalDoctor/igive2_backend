import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './participant-invitation.reducer';
import { IParticipantInvitation } from 'app/shared/model/participant-invitation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IParticipantInvitationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ParticipantInvitationDetail extends React.Component<IParticipantInvitationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { participantInvitationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.participantInvitation.detail.title">ParticipantInvitation</Translate> [
            <b>{participantInvitationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="email">
                <Translate contentKey="igive2App.participantInvitation.email">Email</Translate>
              </span>
            </dt>
            <dd>{participantInvitationEntity.email}</dd>
            <dt>
              <span id="state">
                <Translate contentKey="igive2App.participantInvitation.state">State</Translate>
              </span>
            </dt>
            <dd>{participantInvitationEntity.state ? 'true' : 'false'}</dd>
            <dt>
              <span id="participantId">
                <Translate contentKey="igive2App.participantInvitation.participantId">Participant Id</Translate>
              </span>
            </dt>
            <dd>{participantInvitationEntity.participantId}</dd>
            <dt>
              <Translate contentKey="igive2App.participantInvitation.study">Study</Translate>
            </dt>
            <dd>{participantInvitationEntity.study ? participantInvitationEntity.study.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/participant-invitation" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/participant-invitation/${participantInvitationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ participantInvitation }: IRootState) => ({
  participantInvitationEntity: participantInvitation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ParticipantInvitationDetail);
