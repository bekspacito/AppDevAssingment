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

const initialState = {
	//fetch orders operation data
	orderList    : [],
	listLoading  : false,
	listError    : null,
	//fetch specific order
	order 	     : null,
	loading      : false,
	fetchError   : null,
	//add operation data
	adding 	     : false, //waiting for a response from server
	addError  	 : null,
	//delete operation
	didDelete	 : false,
	deleting 	 : false,
	deleteError  : null,
}

export default function(state=initialState, action){
	switch(action.type){
		case FETCH_ORDERS:
			return {
				...state,
				listLoading : true
			}

		case FETCH_ORDERS_FAILURE:
			return {
				...state,
				...{
					orderList: [],
					listLoading : false,
					listError   : action.errorMsg
				}
			}

		case FETCH_ORDERS_SUCCESS:
			return {
				...state,
				...{
					orderList: action.payload,
					listLoading : false,
					listError   : null
				}
			}
		case ADD_ORDER:
			return {
				...state,
				...{
					uploading : true
				}
			}
		case ADD_ORDER_SUCCESS:
			return {
				...state,
				...{
					uploading : false,
					addError  : null
				}
			}
		case ADD_ORDER_FAILURE:
			return {
				...state,
				...{
					uploading : false,
					addError  : action.payload
				}
			}
		case DELETE_ORDER:
			return {
				...state,
				deleting : true
			}
		case DELETE_ORDER_SUCCESS:
			return {
				...state,
				...{
					orderList : state.orderList.filter(i => i.orderId !== action.payload.id),
					deleting  : false,
					didDelete : true
				}
			}
		case DELETE_ORDER_FAILURE:
			return {
				...state,
				...{
					deleting : false,
					deleteError : action.payload
				}
			}
		default :
			return state; 
	}
}