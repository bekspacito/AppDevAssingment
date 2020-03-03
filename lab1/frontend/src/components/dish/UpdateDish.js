import React, { Component } from "react"
import { connect } from "react-redux"
import { Link } from "react-router-dom"
import { makeStyles } from '@material-ui/core/styles';
import { fetchIngs,fetchIngsByName } from "../../actions/ingredientActions"
import { updateDish,fetchDish } from "../../actions/dishActions"
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
	ingName : "",
	id:"",
	name:"",
	price:"",
	ingredients:[], //{ id name unitname }
	mode : "DISH_INFO" //todo how about dish details
}

class UpdateDish extends Component{
	constructor(props){
		super(props);
		this.state = initState;
		this.setMode = this.setMode.bind(this);
		this.handleChange = this.handleChange.bind(this);
		this.handleSelect = this.handleSelect.bind(this);
		this.handleAmountChange = this.handleAmountChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleDelete = this.handleDelete.bind(this);
		this.handleFetchIngsByName = this.handleFetchIngsByName.bind(this);
	}

	//componentDidMount
	componentDidMount(){
		const {uId} = this.props.match.params;
		//here we send an action/function
		this.props.fetchDish(uId);
	}

	handleFetchIngsByName(ingName){
		if(ingName === "") this.props.fetchIngs();
		else this.props.fetchIngsByName(ingName);
	}

	static getDerivedStateFromProps(props,state){
		if(props.dishToUpd && props.dishToUpd.id !== state.id){
			const ings = props.dishToUpd.ingredients;
			for (let i = 0; i < ings.length; i++) {
				ings[i] = {...ings[i],...{ status : "INIT" }};
			}
			return props.dishToUpd;
		}

		return null;
	}

	handleSubmit(e){
		e.preventDefault();
		
		const ingredients = this.state.ingredients
										.filter(ing => ing.status !== "INIT")
										.map(ing => ({
											id : ing.ingredient.id,
											amount : ing.amount,
											status : ing.status
										}));

		const dishInfo = {
			name  : this.state.name,
			price : this.state.price,
			ingredients : ingredients
		}
		this.props.updateDish(this.state.id,dishInfo,this.props.history);
	}


	handleChange(e){
		this.setState({
			[e.target.name] : e.target.value 
		})
	}

	handleAmountChange(ingId,newAmount){
		const ings = [...this.state.ingredients]
		const i = ings.findIndex(ing => ing.ingredient.id === ingId)
		ings[i] = {
			amount : newAmount,
			ingredient : ings[i].ingredient,
			status : ings[i].status === "NEW" ? "NEW" : "UPD"		
		}
		
		this.setState({	
			ingredients : ings
		})	
	}

	handleSelect(ingId){
		const ings = [...this.state.ingredients];
		const ingToAppend = this.props.list.find(i => i.id === ingId);

		//in DEL state -> unDEL it,set it to UPD
		//in other state -> do not add,because it is already in a list
		//not in a list -> add it to list with status "NEW"
		const ing = ings.find(i => i.ingredient.id === ingId);
		if(ing && ing.status === "DEL"){
			ing.status = "UPD";
		}
		else if(!ing){
			ings.push({
				amount : 0.0,
				ingredient : ingToAppend,
				status : "NEW"
			})
		}

		this.setState({	
			mode : "DISH_INFO",
			ingredients : ings		
		})
	}

	handleDelete(ingId){
		let ings = [...this.state.ingredients];
		const i = ings.findIndex(ing => ing.ingredient.id === ingId);
		switch(ings[i].status){
			case "NEW"  :
				ings = this.state.ingredients.filter(ing => ing.ingredient.id !== ingId)
				break;
			default : 
				ings[i].status = "DEL";
				break;
		}
		this.setState({	
			ingredients : ings
		})	
	}

	setMode(mode){
		this.setState({
			mode : mode
		})
	}

	render(){
		switch(this.state.mode){
			case "ING_SELECT" :
				if(this.props.list.length === 0) this.props.fetchIngs();
				return this.renderIngSelectMode()
			case "DISH_INFO" :
				return this.renderDishInfoMode();
			default : 
				console.log("ВСЕ ХЕРНЯ, ПЕРЕДЕЛЫВАЙ...")
				break;
		}
	}

	renderDishInfoMode(){

		const classes = makeStyles(theme => ({
			  root: {
			    '& .MuiTextField-root': {
			      margin: theme.spacing(1),
			      width: 200,
			    },
			  },
		}));


		return (
			<div className="addDish">
		        <div className="container">
		            <div className="row">
		                <div className="col-md-8 m-auto">
		                    <Link to="/dish" className="btn btn-light">
		                        Back to Dish List
		                    </Link>
		                    <h4 className="display-4 text-center">Add/Update Dish</h4>
		                    <form className={classes.root} onSubmit={this.handleSubmit}>
		                        <div className="form-group">
		                        	<TextField
		                        	 id="name"
						             label="Name"
						             name="name"
						             onChange={this.handleChange}
						             value={this.state.name}
						             variant="filled"
							        />
		                        </div>
		                        <div className="form-group">
		                        	<TextField
		                        	 id="price"
						             label="Price"
						             name="price"
						             onChange={this.handleChange}
						             value={this.state.price}
						             variant="filled"
							        />
		                        </div>
		                        <div className="form-group">
		                        	<TableContainer component={Paper}>
				                      <Table className={classes.table} size="small" aria-label="a dense table">
				                        <TableHead>
				                          <TableRow>
				                            <TableCell>Name</TableCell>
				                            <TableCell>Amount</TableCell>
				                            <TableCell>Price</TableCell>
				                            <TableCell>Unit</TableCell>
				                            <TableCell align="right"></TableCell>                                                
				                          </TableRow>
				                        </TableHead>
				                        <TableBody>
				                          {this.state.ingredients
	                          				.filter(row => row.status !== "DEL")
	                          				.map(row => (
					                            <TableRow key={row.ingredient.id}>
					                              <TableCell component="th" scope="row">
					                                {row.ingredient.name}
					                              </TableCell>
					                              <TableCell component="th" scope="row">
					                                <div className="form-group">
							                        	<TextField value={row.amount} onChange={e => this.handleAmountChange(row.ingredient.id,e.target.value)} variant="filled"/>
			                        				</div>
					                              </TableCell>
					                              <TableCell component="th" scope="row">
					                                {row.ingredient.price}
					                              </TableCell>
					                              <TableCell component="th" scope="row">
					                                {row.ingredient.unit.name}
					                              </TableCell>
					                              <TableCell>       
					                            	<Button onClick={e => this.handleDelete(row.ingredient.id)} variant="contained" color="primary">
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
						        <Button onClick={e => this.setMode("ING_SELECT")} variant="contained" color="primary">
                                	Select ingredient
                            	</Button>
                            	<br /><br />                 	                   
		                        <Button type="submit" variant="contained" color="primary">
                                    Update
                                </Button>
		                    </form>
		                </div>
		            </div>
		        </div>
    		</div>
		)
	}

	renderIngSelectMode(){
            if(this.props.isLoading) return (<div><h1>{"Loading ..."}</h1><br /></div>);
       else if(this.props.error)     return (<div><h1>{"Error ..."}</h1><br /></div>);
       else {    

       		const rows = this.props.list;
            const classes = makeStyles({
                  table: {
                    minWidth: 650,
                  },
            });

			return (<div>
					<TableContainer component={Paper}>
                      <div>
	                      <Button onClick={e => this.setMode("DISH_INFO")} variant="contained" color="primary">
		                  		Back
	              		  </Button>
	              		  <TextField name="ingName" value={this.state.ingName} label="name" onChange={e => this.handleChange(e)} variant="outlined"/>
	          		  	  <Button onClick={e => this.handleFetchIngsByName(this.state.ingName) } variant="contained" color="primary" >
	          		  	  		Find
	          		  	  </Button>
          		  	  </div>	
                      <Table className={classes.table} size="small" aria-label="a dense table">
                        <TableHead>
                          <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell>Unit</TableCell>
                            <TableCell align="right"></TableCell>                                                
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
                              <TableCell component="th" scope="row">
                                {row.unit.name}
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
    list        	: state.ingSection.ingList,
    isLoading   	: state.ingSection.listLoading,
    error       	: state.ingSection.listError,
    dishToUpd		: state.dishSection.dish,
	isDishLoading   : state.dishSection.loading,
	fetchError  	: state.dishSection.fetchError,
})

export default connect(mapStateToProps,{ fetchIngs,fetchIngsByName,updateDish,fetchDish }) ( UpdateDish );