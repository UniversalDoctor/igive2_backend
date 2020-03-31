import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IParticipantInstitution, defaultValue } from 'app/shared/model/participant-institution.model';

export const ACTION_TYPES = {
  FETCH_PARTICIPANTINSTITUTION_LIST: 'participantInstitution/FETCH_PARTICIPANTINSTITUTION_LIST',
  FETCH_PARTICIPANTINSTITUTION: 'participantInstitution/FETCH_PARTICIPANTINSTITUTION',
  CREATE_PARTICIPANTINSTITUTION: 'participantInstitution/CREATE_PARTICIPANTINSTITUTION',
  UPDATE_PARTICIPANTINSTITUTION: 'participantInstitution/UPDATE_PARTICIPANTINSTITUTION',
  DELETE_PARTICIPANTINSTITUTION: 'participantInstitution/DELETE_PARTICIPANTINSTITUTION',
  SET_BLOB: 'participantInstitution/SET_BLOB',
  RESET: 'participantInstitution/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IParticipantInstitution>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ParticipantInstitutionState = Readonly<typeof initialState>;

// Reducer

export default (state: ParticipantInstitutionState = initialState, action): ParticipantInstitutionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PARTICIPANTINSTITUTION):
    case REQUEST(ACTION_TYPES.UPDATE_PARTICIPANTINSTITUTION):
    case REQUEST(ACTION_TYPES.DELETE_PARTICIPANTINSTITUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION):
    case FAILURE(ACTION_TYPES.CREATE_PARTICIPANTINSTITUTION):
    case FAILURE(ACTION_TYPES.UPDATE_PARTICIPANTINSTITUTION):
    case FAILURE(ACTION_TYPES.DELETE_PARTICIPANTINSTITUTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PARTICIPANTINSTITUTION):
    case SUCCESS(ACTION_TYPES.UPDATE_PARTICIPANTINSTITUTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PARTICIPANTINSTITUTION):
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

const apiUrl = 'api/participant-institutions';

// Actions

export const getEntities: ICrudGetAllAction<IParticipantInstitution> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION_LIST,
    payload: axios.get<IParticipantInstitution>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IParticipantInstitution> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PARTICIPANTINSTITUTION,
    payload: axios.get<IParticipantInstitution>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IParticipantInstitution> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PARTICIPANTINSTITUTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IParticipantInstitution> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PARTICIPANTINSTITUTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IParticipantInstitution> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PARTICIPANTINSTITUTION,
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
