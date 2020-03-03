import React, { Component } from "react"
import { Link } from "react-router-dom"
import { connect } from "react-redux"
import { addUnit } from "../../actions/unitActions"
import {
	UNIT_LST
} from "../../utils"
import TextField from '@material-ui/core/TextField';
import { Button } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';

class AddUnit extends Component{

	constructor(props){
		super(props);
		this.state = {
			name : "",
			error : props.error
		}
		this.onChange = this.onChange.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
	}

	componentWillReceiveProps(nextProps){
		this.setState({
			error : nextProps.error
		})
	}

	onChange(e){
		this.setState({
			[e.target.name] : e.target.value 
		})
	}

	onSubmit(e){
		e.preventDefault();
		const unit = {
			name : this.state.name,
		};
		this.props.addUnit(unit, this.props.history);
	}


	render(){
		const error = this.state.error;
		const classes = makeStyles(theme => ({
		  root: {
		    '& .MuiTextField-root': {
		      margin: theme.spacing(1),
		      width: 200,
		    },
		  },
		}));


		return (
			<div className="addUnit">
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

//if errors object changes in store
//we will be notified
const mapStateToProps = state => ({
	error : state.unitSection.addError
})

export default connect(mapStateToProps,{ addUnit })(AddUnit);


