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

const initialState = {
	//fetch units operation data
	unitList     : [],
	listLoading  : false,
	listError    : null,
	//fetch specific unit
	unit         : null,
	loading      : false,
	fetchError   : null,
	//add operation data
	uploading 	 : false,
	addError  	 : null,
	//delete operation
	didDelete	 : false,
	deleting 	 : false,
	deleteError  : null,
	//update operation data
	updating     : false, //when we send data to update
	updateError  : null
}

export default function(state=initialState, action){
	switch(action.type){
		case FETCH_UNITS:
			return {
				...state,
				listLoading : true
			}

		case FETCH_UNITS_FAILURE:
			return {
				...state,
				...{
					unitList: [],
					listLoading : false,
					listError   : action.errorMsg
				}
			}

		case FETCH_UNITS_SUCCESS:
			return {
				...state,
				...{
					unitList: action.payload,
					listLoading : false,
					listError   : null
				}
			}
		case FETCH_UNIT :
			return{
				...state,
				loading : true
			}
		case FETCH_UNIT_SUCCESS:
			return{
				...state,
				...{
					loading : false,
					unit : action.payload
				}
			}
		case FETCH_UNIT_FAILURE: 
			return{
				...state,
				...{
					loading : false,
					fetchError : action.payload
				}
			}
		case ADD_UNIT:
			return {
				...state,
				...{
					uploading : true
				}
			}
		case ADD_UNIT_SUCCESS:
			return {
				...state,
				...{
					uploading : false,
					addError  : null
				}
			}
		case ADD_UNIT_FAILURE:
			return {
				...state,
				...{
					uploading : false,
					addError  : action.payload
				}
			}
		case DELETE_UNIT:
			return {
				...state,
				deleting : true
			}
		case DELETE_UNIT_SUCCESS:
			return {
				...state,
				...{
					deleting  : false,
					didDelete : true
				}
			}
		case DELETE_UNIT_FAILURE:
			return {
				...state,
				...{
					deleting : false,
					deleteError : action.payload
				}
			}
		case DELETE_UNIT_RESET:
			return {
				...state,
				didDelete : false
			}
		case UPDATE_UNIT:
			return {
				...state,
				updating : true
			}
		case UPDATE_UNIT_SUCCESS:
			return {
				...state,
				...{
					updating : false
				}
			}
		case UPDATE_UNIT_FAILURE:
			return {
				...state,
				...{
					updating : false,
					updateError : action.payload
				}
			}
		case UPDATE_UNIT_RESET:
			return {
				...state,
				...{
					unit 		 : null,
					updating     : false,
					updateError  : null
				}
			}

		default :
			return state; 
	}
}