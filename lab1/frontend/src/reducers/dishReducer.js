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

const initialState = {
	//fetch dishes operation data
	dishList      : [],
	listLoading  : false,
	listError    : null,
	//fetch specific dish
	dish 	     : null,
	loading      : false,
	fetchError   : null,
	//add operation data
	adding 	     : false, //waiting for a response from server
	addError  	 : null,
	//delete operation
	didDelete	 : false,
	deleting 	 : false,
	deleteError  : null,
	//update operation
	updating     : false, //when we send data to update
	updateError  : null
}

export default function(state=initialState, action){
	switch(action.type){
		case FETCH_DISHES:
			return {
				...state,
				listLoading : true
			}

		case FETCH_DISHES_FAILURE:
			return {
				...state,
				...{
					dishList: [],
					listLoading : false,
					listError   : action.errorMsg
				}
			}

		case FETCH_DISHES_SUCCESS:
			return {
				...state,
				...{
					dishList: action.payload,
					listLoading : false,
					listError   : null
				}
			}
		case FETCH_DISH :
			return{
				...state,
				loading : true
			}
		case FETCH_DISH_SUCCESS:
			return{
				...state,
				...{
					loading : false,
					dish : action.payload
				}
			}
		case FETCH_DISH_FAILURE: 
			return{
				...state,
				...{
					loading : false,
					fetchError : action.payload
				}
			}
		case ADD_DISH:
			return {
				...state,
				...{
					uploading : true
				}
			}
		case ADD_DISH_SUCCESS:
			return {
				...state,
				...{
					uploading : false,
					addError  : null
				}
			}
		case ADD_DISH_FAILURE:
			return {
				...state,
				...{
					uploading : false,
					addError  : action.payload
				}
			}
		case DELETE_DISH:
			return {
				...state,
				deleting : true
			}
		case DELETE_DISH_SUCCESS:
			return {
				...state,
				...{
					dishList   : state.dishList.filter(i => i.id != action.payload.id),
					deleting  : false,
					didDelete : true
				}
			}
		case DELETE_DISH_FAILURE:
			return {
				...state,
				...{
					deleting : false,
					deleteError : action.payload
				}
			}
		case UPDATE_DISH:
			return {
				...state,
				updating : true
			}
		case UPDATE_DISH_SUCCESS:
			return {
				...state,
				...{
					updating : false,
					dish : null
				}
			}
		case UPDATE_DISH_FAILURE:
			return {
				...state,
				...{
					updating : false,
					updateError : action.payload
				}
			}
		default :
			return state; 
	}
}