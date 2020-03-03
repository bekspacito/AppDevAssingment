import React, { Component } from "react"
import { connect } from "react-redux"
import { Link } from "react-router-dom"
import { makeStyles } from '@material-ui/core/styles';
import { fetchDishes,fetchDishesByName } from "../../actions/dishActions"
import { addOrder } from "../../actions/orderActions"
import { 
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
	Paper,
	Button,
	TextField
} from '@material-ui/core'

const initState = {
	dishName: "",
	dishes :[], //{ id name unitname }
	mode   : "ORDER_INFO"
}

class AddOrder extends Component{
	constructor(props){
		super(props);
		this.state = initState;
		this.setMode = this.setMode.bind(this);
		this.handleSelect = this.handleSelect.bind(this);
		this.handlePortionChange = this.handlePortionChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleDelete = this.handleDelete.bind(this);
		this.handleChange = this.handleChange.bind(this);
	}

	handleSubmit(e){
		e.preventDefault();

		const input = {
			dishes : this.state.dishes.map(dish => ({
				id : dish.id,
				portions : dish.portions
			}))
		}

		this.props.addOrder(input,this.props.history);
	}

	handleFetchDishesByName(dishName){
		if(dishName === "") this.props.fetchDishes();
		else this.props.fetchDishesByName(dishName);
	}

	handleSelect(id){

		const dishes = [...this.state.dishes];
		const dish = this.props.list.find(d => d.id === id);
		dishes.push({...dish,...{ portions : 0 }})

		this.setState({	
			mode : "ORDER_INFO",
			dishes : dishes		
		})

	}

	handleChange(e){
		this.setState({
			[e.target.name] : e.target.value 
		})
	}

	handlePortionChange(id,portions){
		const dishes = [...this.state.dishes];
		const dish = dishes.find(d => d.id === id);
		dish.portions = portions

		this.setState({	
			dishes : dishes		
		})
	}

	handleDelete(ingId){
		this.setState({	
			dishes : this.state.dishes.filter(d => d.id !== ingId)
		})	
	}

	setMode(mode){
		this.setState({
			mode : mode
		})
	}

	render(){
		switch(this.state.mode){
			case "DISH_SELECT" :
				if(this.props.list.length === 0 && this.state.dishName === "") 
					this.props.fetchDishes();
				return this.renderSelectMode()
			case "ORDER_INFO" :
				return this.renderInfoMode();
			default :				
				break;
		}
	}

	renderInfoMode(){

		const classes = makeStyles(theme => ({
			  root: {
			    '& .MuiTextField-root': {
			      margin: theme.spacing(1),
			      width: 200,
			    },
			  },
		}));



		return (
			<div className="addOrder">
		        <div className="container">
		            <div className="row">
		                <div className="col-md-8 m-auto">
		                    <Button onClick={e => this.props.history.push("/order")} variant="contained" color="primary">
                  				Back
              		  		</Button>
		                    <h4 className="display-4 text-center">Make Order</h4>
		                    <form className={classes.root} onSubmit={this.handleSubmit}>
		                        <div className="form-group">
		                        	<TableContainer component={Paper}>
				                      <Table className={classes.table} size="small" aria-label="a dense table">
				                        <TableHead>
				                          <TableRow>				                          	
				                            <TableCell>Name</TableCell>
				                            <TableCell>Price</TableCell>
				                            <TableCell>Portions</TableCell>
				                            <TableCell></TableCell>				                                                                            
				                          </TableRow>
				                        </TableHead>
				                        <TableBody>
				                          {this.state.dishes.map(row => (
				                            <TableRow key={row.id}>
				                              <TableCell component="th" scope="row">
				                                {row.name}
				                              </TableCell>
				                              <TableCell component="th" scope="row">
				                                {row.price}
				                              </TableCell>				                              
				                              <TableCell component="th" scope="row">
				                                <div className="form-group">
						                        	<TextField onChange={e => this.handlePortionChange(row.id,e.target.value)} variant="filled"/>
		                        				</div>
				                              </TableCell>
				                              <TableCell align="left" >
				                                    <Button onClick={e => this.handleDelete(row.id)} variant="contained" color="primary">
				                                        Delete
				                                    </Button>
				                              </TableCell>				                                                            
				                            </TableRow>
				                          ))}
				                        </TableBody>
				                      </Table>
				                    </TableContainer>
		                        </div>		                        
						        <br />
						        <Button onClick={e => this.setMode("DISH_SELECT")} variant="contained" color="primary">
                                	Select dish
                            	</Button>
                            	<br /><br />                 	                   
		                        <Button type="submit" variant="contained" color="primary">
                                    Submit
                                </Button>
		                    </form>
		                </div>
		            </div>
		        </div>
    		</div>
		)
	}

	renderSelectMode(){
            if(this.props.isLoading) return (<div><h1>{"Loading ..."}</h1><br /></div>);
       else if(this.props.error)     return (<div><h1>{"Error ..."}</h1><br /></div>);
       else {    

       		const rows = this.props.list;
            const classes = makeStyles({
                  table: {
                    minWidth: 650,
                  },
            });

            return (
                <div>
                	<div>
	                	<Button onClick={e => this.setMode("ORDER_INFO")} variant="contained" color="primary">
	                  		Back
	          		    </Button>
	          		    <TextField name="dishName" value={this.state.dishName} label="name" onChange={e => this.handleChange(e)} variant="outlined"/>
          		  	    <Button onClick={e => this.handleFetchDishesByName(this.state.dishName) } variant="contained" color="primary" >
          		  	  		Find
          		  	    </Button>
          		    </div>
                    <TableContainer component={Paper}>
                      <Table className={classes.table} size="small" aria-label="a dense table">
                        <TableHead>
                          <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell></TableCell>                            
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {rows.map(row => (
                            <TableRow key={row.id}>
                              <TableCell component="th" scope="row">
                                {row.id}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.name}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.price}
                              </TableCell>
                              <TableCell>       
                            	<Button onClick={e => this.handleSelect(row.id)} variant="contained" color="primary">
	                            	Select
	                        	</Button>
                        	  </TableCell>	     
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                </div>
            )

		}
	}
}

const mapStateToProps = state => ({
    list        : state.dishSection.dishList,
    isLoading   : state.dishSection.listLoading,
    error       : state.dishSection.listError,
})

export default connect(mapStateToProps,{ fetchDishes,fetchDishesByName,addOrder }) ( AddOrder );