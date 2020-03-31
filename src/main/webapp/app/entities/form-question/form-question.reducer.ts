import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFormQuestion, defaultValue } from 'app/shared/model/form-question.model';

export const ACTION_TYPES = {
  FETCH_FORMQUESTION_LIST: 'formQuestion/FETCH_FORMQUESTION_LIST',
  FETCH_FORMQUESTION: 'formQuestion/FETCH_FORMQUESTION',
  CREATE_FORMQUESTION: 'formQuestion/CREATE_FORMQUESTION',
  UPDATE_FORMQUESTION: 'formQuestion/UPDATE_FORMQUESTION',
  DELETE_FORMQUESTION: 'formQuestion/DELETE_FORMQUESTION',
  RESET: 'formQuestion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFormQuestion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FormQuestionState = Readonly<typeof initialState>;

// Reducer

export default (state: FormQuestionState = initialState, action): FormQuestionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FORMQUESTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FORMQUESTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FORMQUESTION):
    case REQUEST(ACTION_TYPES.UPDATE_FORMQUESTION):
    case REQUEST(ACTION_TYPES.DELETE_FORMQUESTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FORMQUESTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FORMQUESTION):
    case FAILURE(ACTION_TYPES.CREATE_FORMQUESTION):
    case FAILURE(ACTION_TYPES.UPDATE_FORMQUESTION):
    case FAILURE(ACTION_TYPES.DELETE_FORMQUESTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FORMQUESTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_FORMQUESTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FORMQUESTION):
    case SUCCESS(ACTION_TYPES.UPDATE_FORMQUESTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FORMQUESTION):
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

const apiUrl = 'api/form-questions';

// Actions

export const getEntities: ICrudGetAllAction<IFormQuestion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FORMQUESTION_LIST,
    payload: axios.get<IFormQuestion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFormQuestion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FORMQUESTION,
    payload: axios.get<IFormQuestion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFormQuestion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FORMQUESTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFormQuestion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FORMQUESTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFormQuestion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FORMQUESTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
