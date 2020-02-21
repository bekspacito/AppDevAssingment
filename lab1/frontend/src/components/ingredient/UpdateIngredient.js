import React, { Component } from "react"
import { connect } from "react-redux"
import { ING_LST } from "../../utils" 
import { Link } from "react-router-dom"
import { makeStyles } from '@material-ui/core/styles';
import { fetchUnits } from "../../actions/unitActions"
import { updateIng,fetchIng } from "../../actions/ingredientActions"
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
import _ from "lodash"

const initState = {
	id:"",
	name:"",
	price:"",
	unit:{
		id : null,
		name : "none"
	},
	isUnitSelectMode : false
}

class UpdateIngredient extends Component{
	constructor(props){
		super(props);
		this.state = initState;
		this.renderIngInfoMode    = this.renderIngInfoMode.bind(this);
		this.renderUnitSelectMode = this.renderUnitSelectMode.bind(this);
		this.handleChange 		  = this.handleChange.bind(this);
		this.setUnitSelectMode    = this.setUnitSelectMode.bind(this);
		this.setIngInfoMode    	  = this.setIngInfoMode.bind(this);
		this.formMessage 		  = this.formMessage.bind(this);
		this.handleSelect		  = this.handleSelect.bind(this);
		this.handleSubmit 		  = this.handleSubmit.bind(this);
	}

	//componentDidMount
	componentDidMount(){
		const {uId} = this.props.match.params;
		//here we send an action/function
		this.props.fetchIng(uId);
	}	

	static getDerivedStateFromProps(props,state){
		if(props.ingToUpd && props.ingToUpd.id !== state.id){
			return props.ingToUpd;
		}

		return null;
	}

	handleSubmit(e){
		e.preventDefault();
		const ingInfo = {
			name  : this.state.name,
			price : this.state.price,
			//why the fuck unit is an array?
			unitId: this.state.unit.id
		}
		this.props.updateIng(this.state.id,ingInfo,this.props.history);
	}

	handleChange(e){
		this.setState({
			[e.target.name] : e.target.value 
		})
	}

	handleSelect(unitId){
		//set unit
		const unit = this.props.list.find(u => u.id === unitId)

		//return to ingInfoMode
		this.setState({
			...this.state,
			...{	
				isUnitSelectMode : false,
				unit : unit
			}
		})
	}

	setUnitSelectMode(){
		if(this.props.list.length === 0)
			this.props.fetchUnits();
		this.setState({ isUnitSelectMode : true })
	}

	setIngInfoMode(){
		this.setState({ isUnitSelectMode : false })	
	}

	formMessage(msg){
        return (<div><h1>{ msg }</h1><br /></div>); 
    }

	render(){
		let mode = this.state.isUnitSelectMode;
		return mode ? this.renderUnitSelectMode() : this.renderIngInfoMode();
	}

	renderIngInfoMode(){

		const classes = makeStyles(theme => ({
			  root: {
			    '& .MuiTextField-root': {
			      margin: theme.spacing(1),
			      width: 200,
			    },
			  },
		}));


		return (
			<div className="addIngredient">
		        <div className="container">
		            <div className="row">
		                <div className="col-md-8 m-auto">
		                    <Link to={ ING_LST } className="btn btn-light">
		                        Back to Ingredient List
		                    </Link>
		                    <h4 className="display-4 text-center">Add/Update Ingredient</h4>
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
		                        	<TextField
						             label="Unit"
						             variant="filled"
							     	 value={this.state.unit.name}
							     	 disabled
							     	/>
		                        </div>		                        
						        <br />
						        <Button onClick={e => this.setUnitSelectMode()} variant="contained" color="primary">
                                	Select unit
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

	renderUnitSelectMode(){
            if(this.props.isLoading) return this.formMessage("Loading ...");
       else if(this.props.error)     return this.formMessage("Error ...");
       else {    

       		const rows = this.props.list;
            const classes = makeStyles({
                  table: {
                    minWidth: 650,
                  },
            });

			return (
				<TableContainer component={Paper}>
	              <Button onClick={e => this.setIngInfoMode()} variant="contained" color="primary">
	                  Back
	              </Button>
	              <Table className={classes.table} size="small" aria-label="a dense table">
	                <TableHead>
	                  <TableRow>
	                    <TableCell>ID</TableCell>
	                    <TableCell>Name</TableCell>
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
	                      <TableCell align="right">
	                        <Button onClick={e => this.handleSelect(row.id)} variant="contained" color="primary">
	                            Select
	                        </Button>
	                      </TableCell>                           
	                    </TableRow>
	                  ))}
	                </TableBody>
	              </Table>
	            </TableContainer>
			)
		}
	}
}

const mapStateToProps = state => ({
    list            : state.unitSection.unitList,
    isListLoading   : state.unitSection.listLoading,
    error           : state.unitSection.listError,
    ingToUpd        : state.ingSection.ing,
    isLoading       : state.ingSection.loading,
	fetchError      : state.ingSection.fetchError,
})

export default connect(mapStateToProps,{ fetchUnits,updateIng,fetchIng }) ( UpdateIngredient );