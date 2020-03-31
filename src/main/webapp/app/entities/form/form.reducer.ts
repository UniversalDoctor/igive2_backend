import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IForm, defaultValue } from 'app/shared/model/form.model';

export const ACTION_TYPES = {
  FETCH_FORM_LIST: 'form/FETCH_FORM_LIST',
  FETCH_FORM: 'form/FETCH_FORM',
  CREATE_FORM: 'form/CREATE_FORM',
  UPDATE_FORM: 'form/UPDATE_FORM',
  DELETE_FORM: 'form/DELETE_FORM',
  RESET: 'form/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IForm>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FormState = Readonly<typeof initialState>;

// Reducer

export default (state: FormState = initialState, action): FormState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FORM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FORM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FORM):
    case REQUEST(ACTION_TYPES.UPDATE_FORM):
    case REQUEST(ACTION_TYPES.DELETE_FORM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FORM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FORM):
    case FAILURE(ACTION_TYPES.CREATE_FORM):
    case FAILURE(ACTION_TYPES.UPDATE_FORM):
    case FAILURE(ACTION_TYPES.DELETE_FORM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FORM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_FORM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FORM):
    case SUCCESS(ACTION_TYPES.UPDATE_FORM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FORM):
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

const apiUrl = 'api/forms';

// Actions

export const getEntities: ICrudGetAllAction<IForm> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FORM_LIST,
    payload: axios.get<IForm>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IForm> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FORM,
    payload: axios.get<IForm>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IForm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FORM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IForm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FORM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IForm> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FORM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
