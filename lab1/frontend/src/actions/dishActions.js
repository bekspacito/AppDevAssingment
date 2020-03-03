import axios from "axios"
import { ROOT_URL } from "../utils"
import {	
 FETCH_DISHES,
 FETCH_DISHES_SUCCESS,
 FETCH_DISHES_FAILURE,

 FETCH_DISH,
 FETCH_DISH_SUCCESS,
 FETCH_DISH_FAILURE,

 ADD_DISH,
 ADD_DISH_SUCCESS,
 ADD_DISH_FAILURE,

 UPDATE_DISH,
 UPDATE_DISH_SUCCESS,
 UPDATE_DISH_FAILURE,

 DELETE_DISH,
 DELETE_DISH_SUCCESS,
 DELETE_DISH_FAILURE

} from "../actions/types"

export const fetchDishes = () => async dispatch => {
	
	dispatch({type : FETCH_DISHES})
	try{
		const result =  await axios.get(`${ROOT_URL}/api/dish/all`);
		dispatch({
			type : FETCH_DISHES_SUCCESS,
			payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_DISHES_FAILURE,
			payload : error.response	
		})
	}
}

export const fetchDishesByName = (partName) => async dispatch => {

	dispatch({type : FETCH_DISHES})
	try{
		const result =  await axios.get(`${ROOT_URL}/api/dish/pname/${partName}`);
		dispatch({
			type : FETCH_DISHES_SUCCESS,
			payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_DISHES_FAILURE,
			payload : error.response	
		})
	}	

}

export const fetchDish = (dishId) => async dispatch => {

	dispatch({type : FETCH_DISH});
	try{
		const result =  await axios.get(`${ROOT_URL}/api/dish/${dishId}`);
		dispatch({
				type : FETCH_DISH_SUCCESS,
				payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_DISH_FAILURE,
			payload : error.response.data	
		})
	}	
}

export const addDish = (dishInfo,history) => async dispatch => {

	dispatch({type : ADD_DISH});
	try{
		await axios.post(`${ROOT_URL}/api/dish`,dishInfo);
		history.push("/dish");
		dispatch({
			type    : ADD_DISH_SUCCESS
		})
	}catch(error){
		dispatch({
			type 	: ADD_DISH_FAILURE,
			error 	: error.response
		})
	}

}

export const updateDish = (dishId,dishInfo,history) => async dispatch => {

	dispatch({type : UPDATE_DISH});
	try{
		await axios.post(`${ROOT_URL}/api/dish/${dishId}`,dishInfo);
		history.push("/dish");
		dispatch({
			type    : UPDATE_DISH_SUCCESS
		})
	}catch(error){
		dispatch({
			type 	: UPDATE_DISH_FAILURE,
			error 	: error.response
		})
	}
}

export const deleteDish = (dishId,history) => async dispatch => {

	dispatch({type : DELETE_DISH})
	try{
		await axios.delete(`${ROOT_URL}/api/dish/${dishId}`);
		dispatch({
			type    : DELETE_DISH_SUCCESS,
			payload : {
				id : dishId
			}
		})
	}catch(error){
		console.log(error)
		dispatch({
			type 	: DELETE_DISH_FAILURE,
			error 	: error.response
		})
	}
	history.push("/dish");
}
