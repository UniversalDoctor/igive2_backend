import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIGive2User, defaultValue } from 'app/shared/model/i-give-2-user.model';

export const ACTION_TYPES = {
  FETCH_IGIVE2USER_LIST: 'iGive2User/FETCH_IGIVE2USER_LIST',
  FETCH_IGIVE2USER: 'iGive2User/FETCH_IGIVE2USER',
  CREATE_IGIVE2USER: 'iGive2User/CREATE_IGIVE2USER',
  UPDATE_IGIVE2USER: 'iGive2User/UPDATE_IGIVE2USER',
  DELETE_IGIVE2USER: 'iGive2User/DELETE_IGIVE2USER',
  RESET: 'iGive2User/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIGive2User>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type IGive2UserState = Readonly<typeof initialState>;

// Reducer

export default (state: IGive2UserState = initialState, action): IGive2UserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_IGIVE2USER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_IGIVE2USER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_IGIVE2USER):
    case REQUEST(ACTION_TYPES.UPDATE_IGIVE2USER):
    case REQUEST(ACTION_TYPES.DELETE_IGIVE2USER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_IGIVE2USER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_IGIVE2USER):
    case FAILURE(ACTION_TYPES.CREATE_IGIVE2USER):
    case FAILURE(ACTION_TYPES.UPDATE_IGIVE2USER):
    case FAILURE(ACTION_TYPES.DELETE_IGIVE2USER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_IGIVE2USER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_IGIVE2USER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_IGIVE2USER):
    case SUCCESS(ACTION_TYPES.UPDATE_IGIVE2USER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_IGIVE2USER):
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

const apiUrl = 'api/i-give-2-users';

// Actions

export const getEntities: ICrudGetAllAction<IIGive2User> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_IGIVE2USER_LIST,
    payload: axios.get<IIGive2User>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IIGive2User> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_IGIVE2USER,
    payload: axios.get<IIGive2User>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIGive2User> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_IGIVE2USER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIGive2User> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_IGIVE2USER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIGive2User> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_IGIVE2USER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
