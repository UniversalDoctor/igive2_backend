import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAnswer, defaultValue } from 'app/shared/model/answer.model';

export const ACTION_TYPES = {
  FETCH_ANSWER_LIST: 'answer/FETCH_ANSWER_LIST',
  FETCH_ANSWER: 'answer/FETCH_ANSWER',
  CREATE_ANSWER: 'answer/CREATE_ANSWER',
  UPDATE_ANSWER: 'answer/UPDATE_ANSWER',
  DELETE_ANSWER: 'answer/DELETE_ANSWER',
  RESET: 'answer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAnswer>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AnswerState = Readonly<typeof initialState>;

// Reducer

export default (state: AnswerState = initialState, action): AnswerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ANSWER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ANSWER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ANSWER):
    case REQUEST(ACTION_TYPES.UPDATE_ANSWER):
    case REQUEST(ACTION_TYPES.DELETE_ANSWER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ANSWER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ANSWER):
    case FAILURE(ACTION_TYPES.CREATE_ANSWER):
    case FAILURE(ACTION_TYPES.UPDATE_ANSWER):
    case FAILURE(ACTION_TYPES.DELETE_ANSWER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANSWER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ANSWER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ANSWER):
    case SUCCESS(ACTION_TYPES.UPDATE_ANSWER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ANSWER):
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

const apiUrl = 'api/answers';

// Actions

export const getEntities: ICrudGetAllAction<IAnswer> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ANSWER_LIST,
    payload: axios.get<IAnswer>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAnswer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ANSWER,
    payload: axios.get<IAnswer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAnswer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ANSWER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAnswer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ANSWER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAnswer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ANSWER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
