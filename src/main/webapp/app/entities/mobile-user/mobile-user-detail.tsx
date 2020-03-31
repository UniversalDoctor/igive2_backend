import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './mobile-user.reducer';
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMobileUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MobileUserDetail extends React.Component<IMobileUserDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { mobileUserEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.mobileUser.detail.title">MobileUser</Translate> [<b>{mobileUserEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="gender">
                <Translate contentKey="igive2App.mobileUser.gender">Gender</Translate>
              </span>
            </dt>
            <dd>{mobileUserEntity.gender}</dd>
            <dt>
              <span id="birthdate">
                <Translate contentKey="igive2App.mobileUser.birthdate">Birthdate</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={mobileUserEntity.birthdate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="diseases">
                <Translate contentKey="igive2App.mobileUser.diseases">Diseases</Translate>
              </span>
            </dt>
            <dd>{mobileUserEntity.diseases}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="igive2App.mobileUser.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{mobileUserEntity.userId}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="igive2App.mobileUser.status">Status</Translate>
              </span>
            </dt>
            <dd>{mobileUserEntity.status}</dd>
            <dt>
              <span id="icon">
                <Translate contentKey="igive2App.mobileUser.icon">Icon</Translate>
              </span>
            </dt>
            <dd>
              {mobileUserEntity.icon ? (
                <div>
                  <a onClick={openFile(mobileUserEntity.iconContentType, mobileUserEntity.icon)}>
                    <img src={`data:${mobileUserEntity.iconContentType};base64,${mobileUserEntity.icon}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {mobileUserEntity.iconContentType}, {byteSize(mobileUserEntity.icon)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="username">
                <Translate contentKey="igive2App.mobileUser.username">Username</Translate>
              </span>
            </dt>
            <dd>{mobileUserEntity.username}</dd>
            <dt>
              <Translate contentKey="igive2App.mobileUser.iGive2User">I Give 2 User</Translate>
            </dt>
            <dd>{mobileUserEntity.iGive2User ? mobileUserEntity.iGive2User.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/mobile-user" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/mobile-user/${mobileUserEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ mobileUser }: IRootState) => ({
  mobileUserEntity: mobileUser.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MobileUserDetail);
