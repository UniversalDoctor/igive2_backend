import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './data.reducer';
import { IData } from 'app/shared/model/data.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDataDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DataDetail extends React.Component<IDataDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { dataEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.data.detail.title">Data</Translate> [<b>{dataEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="data">
                <Translate contentKey="igive2App.data.data">Data</Translate>
              </span>
            </dt>
            <dd>{dataEntity.data}</dd>
            <dt>
              <span id="notes">
                <Translate contentKey="igive2App.data.notes">Notes</Translate>
              </span>
            </dt>
            <dd>{dataEntity.notes}</dd>
            <dt>
              <span id="date">
                <Translate contentKey="igive2App.data.date">Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={dataEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="value">
                <Translate contentKey="igive2App.data.value">Value</Translate>
              </span>
            </dt>
            <dd>{dataEntity.value}</dd>
            <dt>
              <Translate contentKey="igive2App.data.mobileUser">Mobile User</Translate>
            </dt>
            <dd>{dataEntity.mobileUser ? dataEntity.mobileUser.id : ''}</dd>
            <dt>
              <Translate contentKey="igive2App.data.participant">Participant</Translate>
            </dt>
            <dd>{dataEntity.participant ? dataEntity.participant.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/data" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/data/${dataEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ data }: IRootState) => ({
  dataEntity: data.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DataDetail);
