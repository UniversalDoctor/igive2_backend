import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IResearcher, defaultValue } from 'app/shared/model/researcher.model';

export const ACTION_TYPES = {
  FETCH_RESEARCHER_LIST: 'researcher/FETCH_RESEARCHER_LIST',
  FETCH_RESEARCHER: 'researcher/FETCH_RESEARCHER',
  CREATE_RESEARCHER: 'researcher/CREATE_RESEARCHER',
  UPDATE_RESEARCHER: 'researcher/UPDATE_RESEARCHER',
  DELETE_RESEARCHER: 'researcher/DELETE_RESEARCHER',
  RESET: 'researcher/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IResearcher>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ResearcherState = Readonly<typeof initialState>;

// Reducer

export default (state: ResearcherState = initialState, action): ResearcherState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RESEARCHER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESEARCHER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESEARCHER):
    case REQUEST(ACTION_TYPES.UPDATE_RESEARCHER):
    case REQUEST(ACTION_TYPES.DELETE_RESEARCHER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RESEARCHER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESEARCHER):
    case FAILURE(ACTION_TYPES.CREATE_RESEARCHER):
    case FAILURE(ACTION_TYPES.UPDATE_RESEARCHER):
    case FAILURE(ACTION_TYPES.DELETE_RESEARCHER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESEARCHER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESEARCHER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESEARCHER):
    case SUCCESS(ACTION_TYPES.UPDATE_RESEARCHER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESEARCHER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/researchers';

// Actions

export const getEntities: ICrudGetAllAction<IResearcher> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RESEARCHER_LIST,
    payload: axios.get<IResearcher>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IResearcher> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESEARCHER,
    payload: axios.get<IResearcher>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IResearcher> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESEARCHER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IResearcher> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESEARCHER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IResearcher> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESEARCHER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
