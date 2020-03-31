import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './form.reducer';
import { IForm } from 'app/shared/model/form.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFormDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FormDetail extends React.Component<IFormDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { formEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.form.detail.title">Form</Translate> [<b>{formEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="igive2App.form.name">Name</Translate>
              </span>
            </dt>
            <dd>{formEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="igive2App.form.description">Description</Translate>
              </span>
            </dt>
            <dd>{formEntity.description}</dd>
            <dt>
              <span id="state">
                <Translate contentKey="igive2App.form.state">State</Translate>
              </span>
            </dt>
            <dd>{formEntity.state}</dd>
            <dt>
              <Translate contentKey="igive2App.form.study">Study</Translate>
            </dt>
            <dd>{formEntity.study ? formEntity.study.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/form" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/form/${formEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ form }: IRootState) => ({
  formEntity: form.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FormDetail);
