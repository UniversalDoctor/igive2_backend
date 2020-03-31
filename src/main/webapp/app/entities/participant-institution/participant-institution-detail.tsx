import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './participant-institution.reducer';
import { IParticipantInstitution } from 'app/shared/model/participant-institution.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IParticipantInstitutionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ParticipantInstitutionDetail extends React.Component<IParticipantInstitutionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { participantInstitutionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.participantInstitution.detail.title">ParticipantInstitution</Translate> [
            <b>{participantInstitutionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="logo">
                <Translate contentKey="igive2App.participantInstitution.logo">Logo</Translate>
              </span>
            </dt>
            <dd>
              {participantInstitutionEntity.logo ? (
                <div>
                  <a onClick={openFile(participantInstitutionEntity.logoContentType, participantInstitutionEntity.logo)}>
                    <img
                      src={`data:${participantInstitutionEntity.logoContentType};base64,${participantInstitutionEntity.logo}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {participantInstitutionEntity.logoContentType}, {byteSize(participantInstitutionEntity.logo)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <Translate contentKey="igive2App.participantInstitution.study">Study</Translate>
            </dt>
            <dd>{participantInstitutionEntity.study ? participantInstitutionEntity.study.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/participant-institution" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/participant-institution/${participantInstitutionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ participantInstitution }: IRootState) => ({
  participantInstitutionEntity: participantInstitution.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ParticipantInstitutionDetail);
