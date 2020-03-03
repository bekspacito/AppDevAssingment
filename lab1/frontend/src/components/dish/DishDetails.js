import React,{ Component } from "react"
import { Button } from '@material-ui/core';


class DishDetails extends Component{

	render(){
		const dish = this.props.dishToShow;
		const ingList = dish.ingredients.map(ing => {
			return (
				<div key={ing.ingredient.id}>
					<br /><br />
					name    : {ing.ingredient.name}<br />
					price   : {ing.ingredient.price}<br />
					amount  : {ing.amount} {ing.ingredient.unit.name}(s)<br />
				</div>
			)
		})
		const costPrice = dish.ingredients.map(ing => ing.ingredient.price * ing.amount)
								      	  .reduce((total,el) => total += el); 

		return (
			<div>
				<Button onClick={e => this.props.backToList()} variant="contained" color="primary">
                        Back to list
                </Button>
				<br/><br/>
				<div>
					ID : {dish.id}<br />
					NAME : {dish.name}<br />
					PRICE : {dish.price}<br />
					COST PRICE : {costPrice}<br />
					INGREDIENTS : <br />
					<div>
					{
						ingList
					}
					</div>
				</div>
			</div>
		)
	}

} 


export default DishDetails