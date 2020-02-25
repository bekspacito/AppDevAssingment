import React        	from 'react';
import store        	from "./store"
import { Provider } 	from "react-redux"
import UnitsList    	from "./components/unit/UnitsList"
import AddUnit      	from "./components/unit/AddUnit"
import UpdateUnit   	from "./components/unit/UpdateUnit"
import IngredientList 	from "./components/ingredient/IngredientList"
import AddIngredient    from "./components/ingredient/AddIngredient"
import UpdateIngredient from "./components/ingredient/UpdateIngredient" 
import DishList			from "./components/dish/DishList"
import AddDish 			from "./components/dish/AddDish"
import UpdateDish		from "./components/dish/UpdateDish"
import { 
	BrowserRouter as Router, 
	Route
} from "react-router-dom";


function App() {
  return (
  	<Provider store={store}>
	  	<Router>
		    <div className="App">
			    <Route exact path="/unit" component={ UnitsList } />
			    <Route exact path="/unit/add" component={ AddUnit } />
			    <Route exact path="/unit/update/:uId" component={UpdateUnit} />
		    	<Route exact path="/ingredient" component={ IngredientList } />
		    	<Route exact path="/ingredient/add" component={ AddIngredient } />
		    	<Route exact path="/ingredient/update/:uId" component={ UpdateIngredient } />
		    	<Route exact path="/dish" component={ DishList } />
		    	<Route exact path="/dish/add" component={ AddDish } />
		    	<Route exact path="/dish/update/:uId" component={ UpdateDish } />
		    </div>
	    </Router>
    </Provider>
  );
}

export default App;
