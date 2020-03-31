import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './researcher.reducer';
import { IResearcher } from 'app/shared/model/researcher.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IResearcherDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ResearcherDetail extends React.Component<IResearcherDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { researcherEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.researcher.detail.title">Researcher</Translate> [<b>{researcherEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="institution">
                <Translate contentKey="igive2App.researcher.institution">Institution</Translate>
              </span>
            </dt>
            <dd>{researcherEntity.institution}</dd>
            <dt>
              <span id="honorifics">
                <Translate contentKey="igive2App.researcher.honorifics">Honorifics</Translate>
              </span>
            </dt>
            <dd>{researcherEntity.honorifics}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="igive2App.researcher.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{researcherEntity.userId}</dd>
            <dt>
              <Translate contentKey="igive2App.researcher.iGive2User">I Give 2 User</Translate>
            </dt>
            <dd>{researcherEntity.iGive2User ? researcherEntity.iGive2User.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/researcher" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/researcher/${researcherEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ researcher }: IRootState) => ({
  researcherEntity: researcher.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ResearcherDetail);
