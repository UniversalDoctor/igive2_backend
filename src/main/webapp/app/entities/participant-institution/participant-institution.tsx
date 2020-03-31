import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import {
  openFile,
  byteSize,
  Translate,
  ICrudGetAllAction,
  getSortState,
  IPaginationBaseState,
  JhiPagination,
  JhiItemCount
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './participant-institution.reducer';
import { IParticipantInstitution } from 'app/shared/model/participant-institution.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IParticipantInstitutionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IParticipantInstitutionState = IPaginationBaseState;

export class ParticipantInstitution extends React.Component<IParticipantInstitutionProps, IParticipantInstitutionState> {
  state: IParticipantInstitutionState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { participantInstitutionList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="participant-institution-heading">
          <Translate contentKey="igive2App.participantInstitution.home.title">Participant Institutions</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="igive2App.participantInstitution.home.createLabel">Create a new Participant Institution</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {participantInstitutionList && participantInstitutionList.length > 0 ? (
            <Table responsive aria-describedby="participant-institution-heading">
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('logo')}>
                    <Translate contentKey="igive2App.participantInstitution.logo">Logo</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="igive2App.participantInstitution.study">Study</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {participantInstitutionList.map((participantInstitution, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${participantInstitution.id}`} color="link" size="sm">
                        {participantInstitution.id}
                      </Button>
                    </td>
                    <td>
                      {participantInstitution.logo ? (
                        <div>
                          <a onClick={openFile(participantInstitution.logoContentType, participantInstitution.logo)}>
                            <img
                              src={`data:${participantInstitution.logoContentType};base64,${participantInstitution.logo}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                          <span>
                            {participantInstitution.logoContentType}, {byteSize(participantInstitution.logo)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>
                      {participantInstitution.study ? (
                        <Link to={`study/${participantInstitution.study.id}`}>{participantInstitution.study.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${participantInstitution.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${participantInstitution.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${participantInstitution.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="igive2App.participantInstitution.home.notFound">No Participant Institutions found</Translate>
            </div>
          )}
        </div>
        <div className={participantInstitutionList && participantInstitutionList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={this.state.activePage} total={totalItems} itemsPerPage={this.state.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={this.state.activePage}
              onSelect={this.handlePagination}
              maxButtons={5}
              itemsPerPage={this.state.itemsPerPage}
              totalItems={this.props.totalItems}
            />
          </Row>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ participantInstitution }: IRootState) => ({
  participantInstitutionList: participantInstitution.entities,
  totalItems: participantInstitution.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ParticipantInstitution);
