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
import { Button } from '@material-ui/core';
import { 
    fetchDishes,deleteDish
} from "../../actions/dishActions"
import DishDetails from "./DishDetails"

class DishList extends Component{
    constructor(props){
        super(props);
        this.state = {
          showDetails : false,
          dish : null,
        }
        this.handleDelete  = this.handleDelete.bind(this);
        this.handleUpdate  = this.handleUpdate.bind(this);
        this.handleDetails = this.handleDetails.bind(this);
        this.handleBackToList = this.handleBackToList.bind(this);
        this.handleAdd     = this.handleAdd.bind(this);
        this.formMessage   = this.formMessage.bind(this);
    }

    componentDidMount(){
        //async api call to get all the tasks
        this.props.fetchDishes();
    }

    handleUpdate(ingId){
        return e => {
              e.preventDefault();
             // this.props.history.push(`/ingredient/update/${ingId}`);
        }
    }

    handleAdd(){
        return e => {
              e.preventDefault();
              this.props.history.push("/dish/add");
        } 
    }

    handleDelete(dishId){
        return e => {
              e.preventDefault();
              this.props.deleteDish(dishId,this.props.history);
        }
    }

    handleDetails(dishId){
        return e => {
          e.preventDefault();
          this.setState({
              showDetails : true,
              dish : this.props.list.find(d => d.id == dishId)
          })
        }
    }

    handleBackToList(){
        this.setState({
              showDetails : false,
              dish : null
        })
    }

    formMessage(msg){
        return (<div><h1>{ msg }</h1><br /></div>); 
    }

    render(){

            if(this.props.isLoading)   return this.formMessage("Loading ...");
       else if(this.props.error)       return this.formMessage("Error ...");
       else if(this.state.showDetails){
            return <DishDetails dishToShow={this.state.dish} backToList={() => this.handleBackToList()}/>
       }   
       else {
            const rows = this.props.list;
            const classes = makeStyles({
                  table: {
                    minWidth: 650,
                  },
            });
            
            return (
                <div>
                    <Button onClick={this.handleAdd()} variant="contained" color="primary">
                        Add New Dish
                    </Button>
                    <TableContainer component={Paper}>
                      <Table className={classes.table} size="small" aria-label="a dense table">
                        <TableHead>
                          <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
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
                              <TableCell align="left" >
                                    <Button onClick={this.handleDetails(row.id)} variant="contained" color="primary">
                                        Details
                                    </Button>
                              </TableCell>
                              <TableCell align="left" >
                                    <Button onClick={this.handleDelete(row.id)} variant="contained" color="primary">
                                        Delete
                                    </Button>
                              </TableCell>
                              <TableCell align="left" >
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
    list        : state.dishSection.dishList,
    isLoading   : state.dishSection.listLoading,
    error       : state.dishSection.listError
})

//getBacklog is an action creator
//it will be wrapped inside a function with a body like : 
// () => store.dispatch(getBacklog())
//next things is that dispatched action will be caught by and executed by thunk
export default connect(mapStateToProps,{ fetchDishes,deleteDish })(DishList);
