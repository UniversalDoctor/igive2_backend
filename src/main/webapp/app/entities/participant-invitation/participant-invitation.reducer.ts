import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IParticipantInvitation, defaultValue } from 'app/shared/model/participant-invitation.model';

export const ACTION_TYPES = {
  FETCH_PARTICIPANTINVITATION_LIST: 'participantInvitation/FETCH_PARTICIPANTINVITATION_LIST',
  FETCH_PARTICIPANTINVITATION: 'participantInvitation/FETCH_PARTICIPANTINVITATION',
  CREATE_PARTICIPANTINVITATION: 'participantInvitation/CREATE_PARTICIPANTINVITATION',
  UPDATE_PARTICIPANTINVITATION: 'participantInvitation/UPDATE_PARTICIPANTINVITATION',
  DELETE_PARTICIPANTINVITATION: 'participantInvitation/DELETE_PARTICIPANTINVITATION',
  RESET: 'participantInvitation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IParticipantInvitation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ParticipantInvitationState = Readonly<typeof initialState>;

// Reducer

export default (state: ParticipantInvitationState = initialState, action): ParticipantInvitationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PARTICIPANTINVITATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARTICIPANTINVITATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PARTICIPANTINVITATION):
    case REQUEST(ACTION_TYPES.UPDATE_PARTICIPANTINVITATION):
    case REQUEST(ACTION_TYPES.DELETE_PARTICIPANTINVITATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PARTICIPANTINVITATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARTICIPANTINVITATION):
    case FAILURE(ACTION_TYPES.CREATE_PARTICIPANTINVITATION):
    case FAILURE(ACTION_TYPES.UPDATE_PARTICIPANTINVITATION):
    case FAILURE(ACTION_TYPES.DELETE_PARTICIPANTINVITATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTICIPANTINVITATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTICIPANTINVITATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PARTICIPANTINVITATION):
    case SUCCESS(ACTION_TYPES.UPDATE_PARTICIPANTINVITATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PARTICIPANTINVITATION):
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

const apiUrl = 'api/participant-invitations';

// Actions

export const getEntities: ICrudGetAllAction<IParticipantInvitation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PARTICIPANTINVITATION_LIST,
    payload: axios.get<IParticipantInvitation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IParticipantInvitation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PARTICIPANTINVITATION,
    payload: axios.get<IParticipantInvitation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IParticipantInvitation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PARTICIPANTINVITATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IParticipantInvitation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PARTICIPANTINVITATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IParticipantInvitation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PARTICIPANTINVITATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
