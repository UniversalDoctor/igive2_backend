import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import {
  openFile,
  byteSize,
  Translate,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  JhiPagination,
  JhiItemCount
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './mobile-user.reducer';
import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IMobileUserProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IMobileUserState = IPaginationBaseState;

export class MobileUser extends React.Component<IMobileUserProps, IMobileUserState> {
  state: IMobileUserState = {
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
    const { mobileUserList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="mobile-user-heading">
          <Translate contentKey="igive2App.mobileUser.home.title">Mobile Users</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="igive2App.mobileUser.home.createLabel">Create a new Mobile User</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {mobileUserList && mobileUserList.length > 0 ? (
            <Table responsive aria-describedby="mobile-user-heading">
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('gender')}>
                    <Translate contentKey="igive2App.mobileUser.gender">Gender</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('birthdate')}>
                    <Translate contentKey="igive2App.mobileUser.birthdate">Birthdate</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('diseases')}>
                    <Translate contentKey="igive2App.mobileUser.diseases">Diseases</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('userId')}>
                    <Translate contentKey="igive2App.mobileUser.userId">User Id</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('status')}>
                    <Translate contentKey="igive2App.mobileUser.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('icon')}>
                    <Translate contentKey="igive2App.mobileUser.icon">Icon</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('username')}>
                    <Translate contentKey="igive2App.mobileUser.username">Username</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="igive2App.mobileUser.iGive2User">I Give 2 User</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {mobileUserList.map((mobileUser, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${mobileUser.id}`} color="link" size="sm">
                        {mobileUser.id}
                      </Button>
                    </td>
                    <td>
                      <Translate contentKey={`igive2App.GenderType.${mobileUser.gender}`} />
                    </td>
                    <td>
                      <TextFormat type="date" value={mobileUser.birthdate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>
                      <Translate contentKey={`igive2App.Diseases.${mobileUser.diseases}`} />
                    </td>
                    <td>{mobileUser.userId}</td>
                    <td>{mobileUser.status}</td>
                    <td>
                      {mobileUser.icon ? (
                        <div>
                          <a onClick={openFile(mobileUser.iconContentType, mobileUser.icon)}>
                            <img src={`data:${mobileUser.iconContentType};base64,${mobileUser.icon}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                          <span>
                            {mobileUser.iconContentType}, {byteSize(mobileUser.icon)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{mobileUser.username}</td>
                    <td>
                      {mobileUser.iGive2User ? (
                        <Link to={`i-give-2-user/${mobileUser.iGive2User.id}`}>{mobileUser.iGive2User.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${mobileUser.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${mobileUser.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${mobileUser.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="igive2App.mobileUser.home.notFound">No Mobile Users found</Translate>
            </div>
          )}
        </div>
        <div className={mobileUserList && mobileUserList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ mobileUser }: IRootState) => ({
  mobileUserList: mobileUser.entities,
  totalItems: mobileUser.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MobileUser);
