import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMobileUser, defaultValue } from 'app/shared/model/mobile-user.model';

export const ACTION_TYPES = {
  FETCH_MOBILEUSER_LIST: 'mobileUser/FETCH_MOBILEUSER_LIST',
  FETCH_MOBILEUSER: 'mobileUser/FETCH_MOBILEUSER',
  CREATE_MOBILEUSER: 'mobileUser/CREATE_MOBILEUSER',
  UPDATE_MOBILEUSER: 'mobileUser/UPDATE_MOBILEUSER',
  DELETE_MOBILEUSER: 'mobileUser/DELETE_MOBILEUSER',
  SET_BLOB: 'mobileUser/SET_BLOB',
  RESET: 'mobileUser/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMobileUser>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type MobileUserState = Readonly<typeof initialState>;

// Reducer

export default (state: MobileUserState = initialState, action): MobileUserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MOBILEUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MOBILEUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MOBILEUSER):
    case REQUEST(ACTION_TYPES.UPDATE_MOBILEUSER):
    case REQUEST(ACTION_TYPES.DELETE_MOBILEUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MOBILEUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MOBILEUSER):
    case FAILURE(ACTION_TYPES.CREATE_MOBILEUSER):
    case FAILURE(ACTION_TYPES.UPDATE_MOBILEUSER):
    case FAILURE(ACTION_TYPES.DELETE_MOBILEUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOBILEUSER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOBILEUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MOBILEUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_MOBILEUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MOBILEUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/mobile-users';

// Actions

export const getEntities: ICrudGetAllAction<IMobileUser> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MOBILEUSER_LIST,
    payload: axios.get<IMobileUser>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IMobileUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MOBILEUSER,
    payload: axios.get<IMobileUser>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMobileUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MOBILEUSER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMobileUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MOBILEUSER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMobileUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MOBILEUSER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
