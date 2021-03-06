import React, { Component } from "react"
import { Link } from "react-router-dom"
import { connect } from "react-redux"
import { fetchUnit,updateUnit,resetUpdate } from "../../actions/unitActions"
import _ from "lodash"
import { 
	Button,
	TextField
} from '@material-ui/core'



const initState = {
	id : "",
	name : ""
}

class UpdateUnit extends Component{

	constructor(props){
		super(props);
		this.state = initState;
		this.onChange = this.onChange.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
	}

	componentDidMount(){
		const {uId} = this.props.match.params;
		//here we send an action/function
		this.props.fetchUnit(uId);
	}

	onChange(e){
		this.setState({
			[e.target.name] : e.target.value 
		});
	}

	static getDerivedStateFromProps(props,state){
		//map unitToUpd to state first time
		if(props.unitToUpd && _.isEqual(initState,state)){
			return props.unitToUpd;
		}
		//do nothing later
		return null;
	}

	componentWillUnmount(){
		this.props.resetUpdate();
	}

	onSubmit(e){
		e.preventDefault();
		const updatedUnit = {
			id   : this.state.id,
			name : this.state.name,
		};
		this.props.updateUnit(updatedUnit, this.props.history);
	}

	render(){
		if(this.props.isLoading ) return (<div><h1>Loading...</h1><br /></div>);
   else if(this.props.fetchError) return (<div><h1>{this.props.fetchError}</h1><br /></div>)
   else	{
			return (
				<div className="updateUnit">
			        <div className="container">
			            <div className="row">
			                <div className="col-md-8 m-auto">
			                    <Button onClick={e => this.props.history.push("/unit")} variant="contained" color="primary">
		                  				Back
	              		  		</Button>
			                    <h4 className="display-4 text-center">Add/Update Unit</h4>
			                    <form onSubmit={this.onSubmit}>
			                        <div className="form-group">
	                            	    <div className="form-group">
				                        	<TextField
				                        	 id="name"
								             label="Name"
								             name="name"
								             onChange={this.onChange}
								             value={this.state.name}
								             variant="filled"
									        />
		                        		</div>
			                        </div>
			                         <Button type="submit" variant="contained" color="primary">
                                   		 Add
                                	</Button>
			                    </form>
			                </div>
			            </div>
			        </div>
				</div>
			)
	  }
	}

}

const mapStateToProps = state => ({
	unitToUpd  : state.unitSection.unit,
	isLoading  : state.unitSection.loading,
	fetchError : state.unitSection.fetchError,
})

export default connect(mapStateToProps,{ fetchUnit , updateUnit , resetUpdate })(UpdateUnit);