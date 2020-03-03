import axios from "axios"
import { ROOT_URL } from "../utils"
import {	

 FETCH_ORDERS,
 FETCH_ORDERS_SUCCESS,
 FETCH_ORDERS_FAILURE,

 ADD_ORDER,
 ADD_ORDER_SUCCESS,
 ADD_ORDER_FAILURE,

 DELETE_ORDER,
 DELETE_ORDER_SUCCESS,
 DELETE_ORDER_FAILURE

} from "../actions/types"

export const fetchOrders = () => async dispatch => {
	
	dispatch({type : FETCH_ORDERS})
	try{
		const result =  await axios.get(`${ROOT_URL}/api/order/all`);
		dispatch({
			type : FETCH_ORDERS_SUCCESS,
			payload : result.data
		})
	}catch(error){
		dispatch({
			type : FETCH_ORDERS_FAILURE,
			payload : error.response	
		})
	}
}

export const fetchOrdersById = (orderId) => async dispatch => {
	
	dispatch({type : FETCH_ORDERS})
	try{
		const result =  await axios.get(`${ROOT_URL}/api/order/${orderId}`);
		const res = [];
		res.push(result.data);
		dispatch({
			type : FETCH_ORDERS_SUCCESS,
			payload : res
		})
	}catch(error){
		dispatch({
			type : FETCH_ORDERS_FAILURE,
			payload : error.response	
		})
	}

}

export const addOrder = (orderInfo,history) => async dispatch => {

	dispatch({type : ADD_ORDER});
	try{
		await axios.post(`${ROOT_URL}/api/order`,orderInfo);
		history.push("/order");
		dispatch({
			type    : ADD_ORDER_SUCCESS
		})
	}catch(error){
		dispatch({
			type 	: ADD_ORDER_FAILURE,
			error 	: error.response
		})
	}

}

export const deleteOrder = (orderId,history) => async dispatch => {

	dispatch({type : DELETE_ORDER})
	try{
		await axios.delete(`${ROOT_URL}/api/order/${orderId}`);
		dispatch({
			type    : DELETE_ORDER_SUCCESS,
			payload : {
				id : orderId
			}
		})
	}catch(error){
		console.log(error)
		dispatch({
			type 	: DELETE_ORDER_FAILURE,
			error 	: error.response
		})
	}
	history.push("/order");
}


