import axios from "axios"
import { ROOT_URL } from "../utils"
import {
	FETCH_UNITS,
	FETCH_UNITS_SUCCESS,
	FETCH_UNITS_FAILURE,
	
	FETCH_UNIT,
	FETCH_UNIT_SUCCESS,
	FETCH_UNIT_FAILURE,
	
	ADD_UNIT,
	ADD_UNIT_SUCCESS,
	ADD_UNIT_FAILURE,

	DELETE_UNIT,
	DELETE_UNIT_SUCCESS,
	DELETE_UNIT_FAILURE,
	DELETE_UNIT_RESET,

	UPDATE_UNIT,
	UPDATE_UNIT_SUCCESS,
	UPDATE_UNIT_FAILURE,
	UPDATE_UNIT_RESET
} from "../actions/types"


export const addUnit = (unitInfo,history) => async dispatch => {

	dispatch({type : ADD_UNIT});
	try{
		await axios.post(`${ROOT_URL}/api/unit`,unitInfo);
		history.push("/unit");
		dispatch({
			type    : ADD_UNIT_SUCCESS
		})
	}catch(error){
		dispatch({
			type 	: ADD_UNIT_FAILURE,
			error 	: error.response
		})
	}

}

export const deleteUnit = (unitId,history) => async dispatch => {

	dispatch({type : DELETE_UNIT})
	try{
		await axios.delete(`${ROOT_URL}/api/unit/${unitId}`);
		dispatch({
			type    : DELETE_UNIT_SUCCESS
		})
	}catch(error){
		console.log(error)
		dispatch({
			type 	: DELETE_UNIT_FAILURE,
			error 	: error.response
		})
	}
	history.push("/unit");
}

export const resetDidDelete = () => {
    return {
        type : DELETE_UNIT_RESET
    }
}

export const fetchUnits = () => async dispatch => {

	dispatch({type : FETCH_UNITS});
	try{
		const result =  await axios.get(`${ROOT_URL}/api/unit/all`);
		dispatch({
			type : FETCH_UNITS_SUCCESS,
			payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_UNITS_FAILURE,
			payload : error.response	
		})
	}

}

export const fetchUnitsByName = (unitName) => async dispatch => {

	dispatch({type : FETCH_UNITS});
	try{
		const result =  await axios.get(`${ROOT_URL}/api/unit/pname/${unitName}`);
		dispatch({
			type : FETCH_UNITS_SUCCESS,
			payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_UNITS_FAILURE,
			payload : error.response	
		})
	}

}

export const fetchUnit = (unitId) => async dispatch => {

	dispatch({type : FETCH_UNIT});
	try{
		const result =  await axios.get(`${ROOT_URL}/api/unit/${unitId}`);
		dispatch({
				type : FETCH_UNIT_SUCCESS,
				payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_UNIT_FAILURE,
			payload : error.response.data	
		})
	}	
}

export const updateUnit = (unit,history) => async dispatch => {

	dispatch({type : UPDATE_UNIT});
	try{
		await axios.post(`${ROOT_URL}/api/unit/${unit.id}`,unit);
		history.push("/unit");
		dispatch({
			type    : UPDATE_UNIT_SUCCESS,
			payload : unit
		})
	}catch(error){
		dispatch({
			type 	: UPDATE_UNIT_FAILURE,
			error 	: error.response
		})
	}

}

export const resetUpdate = () => {
	return {
		type : UPDATE_UNIT_RESET
	}
}