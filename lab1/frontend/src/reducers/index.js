import { combineReducers } from "redux"
import unitReducer from "./unitReducer"
import ingReducer from "./ingReducer"
import dishReducer from "./dishReducer"

//every field here is an object itself
//error is an object and you can check what object it is inside 
//errorReducer.js
export default combineReducers({
	//reducers are combined here
	unitSection : unitReducer,
	ingSection  : ingReducer,
	dishSection : dishReducer
})