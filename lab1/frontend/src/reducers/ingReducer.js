import {
	
	FETCH_INGS,
    FETCH_INGS_SUCCESS,
    FETCH_INGS_FAILURE,

    FETCH_ING,
    FETCH_ING_SUCCESS,
    FETCH_ING_FAILURE,

    DELETE_ING,
	DELETE_ING_SUCCESS,
	DELETE_ING_FAILURE,

	UPDATE_ING,
	UPDATE_ING_SUCCESS,
	UPDATE_ING_FAILURE

} from "../actions/types"

const initialState = {
	//fetch units operation data
	ingList      : [],
	listLoading  : false,
	listError    : null,
	//fetch specific ingredient
	ing 	     : null,
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
		case FETCH_INGS:
			return {
				...state,
				listLoading : true
			}

		case FETCH_INGS_FAILURE:
			return {
				...state,
				...{
					ingList: [],
					listLoading : false,
					listError   : action.errorMsg
				}
			}

		case FETCH_INGS_SUCCESS:
			return {
				...state,
				...{
					ingList: action.payload,
					listLoading : false,
					listError   : null
				}
			}
		case FETCH_ING :
			return{
				...state,
				loading : true
			}
		case FETCH_ING_SUCCESS:
			return{
				...state,
				...{
					loading : false,
					ing : action.payload
				}
			}
		case FETCH_ING_FAILURE: 
			return{
				...state,
				...{
					loading : false,
					fetchError : action.payload
				}
			}
		case DELETE_ING:
			return {
				...state,
				deleting : true
			}
		case DELETE_ING_SUCCESS:
			return {
				...state,
				...{
					ingList   : state.ingList.filter(i => i.id != action.payload.id),
					deleting  : false,
					didDelete : true
				}
			}
		case DELETE_ING_FAILURE:
			return {
				...state,
				...{
					deleting : false,
					deleteError : action.payload
				}
			}
		case UPDATE_ING:
			return {
				...state,
				updating : true
			}
		case UPDATE_ING_SUCCESS:
			return {
				...state,
				...{
					updating : false
				}
			}
		case UPDATE_ING_FAILURE:
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