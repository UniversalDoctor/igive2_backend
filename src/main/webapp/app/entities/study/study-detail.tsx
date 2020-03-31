import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './study.reducer';
import { IStudy } from 'app/shared/model/study.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStudyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StudyDetail extends React.Component<IStudyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { studyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="igive2App.study.detail.title">Study</Translate> [<b>{studyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="code">
                <Translate contentKey="igive2App.study.code">Code</Translate>
              </span>
            </dt>
            <dd>{studyEntity.code}</dd>
            <dt>
              <span id="icon">
                <Translate contentKey="igive2App.study.icon">Icon</Translate>
              </span>
            </dt>
            <dd>
              {studyEntity.icon ? (
                <div>
                  <a onClick={openFile(studyEntity.iconContentType, studyEntity.icon)}>
                    <img src={`data:${studyEntity.iconContentType};base64,${studyEntity.icon}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {studyEntity.iconContentType}, {byteSize(studyEntity.icon)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="name">
                <Translate contentKey="igive2App.study.name">Name</Translate>
              </span>
            </dt>
            <dd>{studyEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="igive2App.study.description">Description</Translate>
              </span>
            </dt>
            <dd>{studyEntity.description}</dd>
            <dt>
              <span id="moreInfo">
                <Translate contentKey="igive2App.study.moreInfo">More Info</Translate>
              </span>
            </dt>
            <dd>{studyEntity.moreInfo}</dd>
            <dt>
              <span id="contactEmail">
                <Translate contentKey="igive2App.study.contactEmail">Contact Email</Translate>
              </span>
            </dt>
            <dd>{studyEntity.contactEmail}</dd>
            <dt>
              <span id="startDate">
                <Translate contentKey="igive2App.study.startDate">Start Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={studyEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="endDate">
                <Translate contentKey="igive2App.study.endDate">End Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={studyEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="state">
                <Translate contentKey="igive2App.study.state">State</Translate>
              </span>
            </dt>
            <dd>{studyEntity.state}</dd>
            <dt>
              <span id="recruiting">
                <Translate contentKey="igive2App.study.recruiting">Recruiting</Translate>
              </span>
            </dt>
            <dd>{studyEntity.recruiting ? 'true' : 'false'}</dd>
            <dt>
              <span id="requestedData">
                <Translate contentKey="igive2App.study.requestedData">Requested Data</Translate>
              </span>
            </dt>
            <dd>{studyEntity.requestedData}</dd>
            <dt>
              <span id="dataJustification">
                <Translate contentKey="igive2App.study.dataJustification">Data Justification</Translate>
              </span>
            </dt>
            <dd>{studyEntity.dataJustification}</dd>
            <dt>
              <Translate contentKey="igive2App.study.researcher">Researcher</Translate>
            </dt>
            <dd>{studyEntity.researcher ? studyEntity.researcher.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/study" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/study/${studyEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ study }: IRootState) => ({
  studyEntity: study.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StudyDetail);
