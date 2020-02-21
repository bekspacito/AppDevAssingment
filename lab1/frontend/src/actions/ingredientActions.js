import axios from "axios"
import { ROOT_URL } from "../utils"
import {

	FETCH_INGS,
    FETCH_INGS_SUCCESS,
    FETCH_INGS_FAILURE,

    FETCH_ING,
    FETCH_ING_SUCCESS,
    FETCH_ING_FAILURE,

	ADD_ING,
	ADD_ING_SUCCESS,
	ADD_ING_FAILURE,

	DELETE_ING,
	DELETE_ING_SUCCESS,
	DELETE_ING_FAILURE,

	UPDATE_ING,
	UPDATE_ING_SUCCESS,
	UPDATE_ING_FAILURE

} from "../actions/types"


export const fetchIngs = () => async dispatch => {
	
	dispatch({type : FETCH_INGS})
	try{
		const result =  await axios.get(`${ROOT_URL}/api/ingredient/all`);
		dispatch({
			type : FETCH_INGS_SUCCESS,
			payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_INGS_FAILURE,
			payload : error.response	
		})
	}
}

export const fetchIng = (ingId) => async dispatch => {

	dispatch({type : FETCH_ING});
	try{
		const result =  await axios.get(`${ROOT_URL}/api/ingredient/${ingId}`);
		dispatch({
				type : FETCH_ING_SUCCESS,
				payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_ING_FAILURE,
			payload : error.response.data	
		})
	}	
}

export const addIng = (ingInfo,history) => async dispatch => {

	dispatch({type : ADD_ING});
	try{
		await axios.post(`${ROOT_URL}/api/ingredient`,ingInfo);
		history.push("/ingredient");
		dispatch({
			type    : ADD_ING_SUCCESS
		})
	}catch(error){
		dispatch({
			type 	: ADD_ING_FAILURE,
			error 	: error.response
		})
	}

}

export const updateIng = (ingId,ingInfo,history) => async dispatch => {

	dispatch({type : UPDATE_ING});
	try{
		await axios.post(`${ROOT_URL}/api/ingredient/${ingId}`,ingInfo);
		history.push("/ingredient");
		dispatch({
			type    : UPDATE_ING_SUCCESS
		})
	}catch(error){
		dispatch({
			type 	: UPDATE_ING_FAILURE,
			error 	: error.response
		})
	}
}

export const deleteIng = (ingId,history) => async dispatch => {

	dispatch({type : DELETE_ING})
	try{
		await axios.delete(`${ROOT_URL}/api/ingredient/${ingId}`);
		dispatch({
			type    : DELETE_ING_SUCCESS,
			payload : {
				id : ingId
			}
		})
	}catch(error){
		console.log(error)
		dispatch({
			type 	: DELETE_ING_FAILURE,
			error 	: error.response
		})
	}
	history.push("/ingredient");
}
