import React,{ Component } from "react"
import { Button } from '@material-ui/core';


class OrderDetails extends Component{

	render(){
		const order = this.props.orderToShow;
		const dishList = order.dishes.map(dish => {
			return (
				<div key={dish.id}>
					<br /><br />
					name      : {dish.name}<br />
					price     : {dish.price}<br />
					portions  : {dish.portions} <br />
				</div>
			)
		})		

		return (
			<div>
				<Button onClick={e => this.props.backToList()} variant="contained" color="primary">
                    Back to Orders
                </Button>
				<br/><br/>
				<div>
					ID    : {order.orderId}<br />
					PRICE : {order.orderPrice}<br />
					DATE  : {(new Date(order.orderDate)).toLocaleString()}<br />
					PRIME COST : {priceCost}<br />
					DISHES : 
					<div>
					{
						dishList
					}
					</div>
				</div>
			</div>
		)
	}

} 


export default OrderDetails