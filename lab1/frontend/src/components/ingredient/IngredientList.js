import React,{ Component } from "react"
import { connect } from "react-redux"
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import { Button,TextField } from '@material-ui/core';
import { 
    fetchIngs,deleteIng,fetchIngsByName
} from "../../actions/ingredientActions"

class IngredientList extends Component{
    constructor(props){
        super(props)
        this.state = {
          name : ""
        }
        this.handleDelete = this.handleDelete.bind(this);
        this.handleUpdate = this.handleUpdate.bind(this);
        this.handleAdd    = this.handleAdd.bind(this);
        this.formMessage  = this.formMessage.bind(this);
        this.handleFetchIngsByName = this.handleFetchIngsByName.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount(){
        //async api call to get all the tasks
        this.props.fetchIngs();
    }

    handleUpdate(ingId){
        return e => {
              e.preventDefault();
              this.props.history.push(`/ingredient/update/${ingId}`);
        }
    }

    handleChange(e){
      this.setState({
        [e.target.name] : e.target.value 
      })
    }

    handleFetchIngsByName(ingName){
      if(ingName === "") this.props.fetchIngs();
      else this.props.fetchIngsByName(ingName);
    }

    handleAdd(){
        return e => {
              e.preventDefault();
              this.props.history.push("/ingredient/add");
        } 
    }

    handleDelete(ingId){
        return e => {
              e.preventDefault();
              this.props.deleteIng(ingId,this.props.history);
        }
    }

    formMessage(msg){
        return (<div><h1>{ msg }</h1><br /></div>); 
    }

    render(){

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
                <div>
                    <div>
                      <Button onClick={this.handleAdd()} variant="contained" color="primary">
                          Add New Ingredient
                      </Button>
                      <TextField name="name" value={this.state.name} label="name" onChange={e => this.handleChange(e)} variant="outlined"/>
                      <Button onClick={e => this.handleFetchIngsByName(this.state.name) } variant="contained" color="primary" >
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
                            <TableCell>Unit</TableCell>
                            <TableCell align="right"></TableCell>
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
                              <TableCell align="right">
                                    <Button onClick={this.handleDelete(row.id)} variant="contained" color="primary">
                                        Delete
                                    </Button>
                              </TableCell>
                              <TableCell align="right">
                                    <Button onClick={this.handleUpdate(row.id)} variant="contained" color="primary">
                                        Update
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
    list        : state.ingSection.ingList,
    isLoading   : state.ingSection.listLoading,
    error       : state.ingSection.listError
})

//getBacklog is an action creator
//it will be wrapped inside a function with a body like : 
// () => store.dispatch(getBacklog())
//next things is that dispatched action will be caught by and executed by thunk
export default connect(mapStateToProps,{ fetchIngs,fetchIngsByName,deleteIng })(IngredientList);
