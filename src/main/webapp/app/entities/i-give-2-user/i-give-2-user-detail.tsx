import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './i-give-2-user.reducer';
import { IIGive2User } from 'app/shared/model/i-give-2-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIGive2UserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class IGive2UserDetail extends React.Component<IIGive2UserDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { iGive2UserEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.iGive2User.detail.title">IGive2User</Translate> [<b>{iGive2UserEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="newsletter">
                <Translate contentKey="igive2App.iGive2User.newsletter">Newsletter</Translate>
              </span>
            </dt>
            <dd>{iGive2UserEntity.newsletter ? 'true' : 'false'}</dd>
            <dt>
              <span id="termsAccepted">
                <Translate contentKey="igive2App.iGive2User.termsAccepted">Terms Accepted</Translate>
              </span>
            </dt>
            <dd>{iGive2UserEntity.termsAccepted ? 'true' : 'false'}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="igive2App.iGive2User.country">Country</Translate>
              </span>
            </dt>
            <dd>{iGive2UserEntity.country}</dd>
          </dl>
          <Button tag={Link} to="/i-give-2-user" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/i-give-2-user/${iGive2UserEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ iGive2User }: IRootState) => ({
  iGive2UserEntity: iGive2User.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IGive2UserDetail);
