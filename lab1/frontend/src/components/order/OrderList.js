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
    fetchOrders,fetchOrdersById,deleteOrder
} from "../../actions/orderActions"
import OrderDetails from "./OrderDetails"

class OrderList extends Component{
    constructor(props){
        super(props);
        this.state = {
          orderId : "",
          showDetails : false,
          order : null
        }
        this.handleDelete  = this.handleDelete.bind(this);
        this.handleDetails = this.handleDetails.bind(this);
        this.handleBackToList = this.handleBackToList.bind(this);
        this.handleAdd     = this.handleAdd.bind(this);
        this.formMessage   = this.formMessage.bind(this);
        this.handleChange  = this.handleChange.bind(this);
    }

    handleChange(e){
        this.setState({
          [e.target.name] : e.target.value 
        })
    }

    componentDidMount(){
        //async api call to get all the tasks
        this.props.fetchOrders();
    }

    handleAdd(){
        return e => {
              e.preventDefault();
              this.props.history.push("/order/add");
        } 
    }

    handleDelete(orderId){
        return e => {
              e.preventDefault();
              this.props.deleteOrder(orderId,this.props.history);
        }
    }

    handleDetails(id){
        return e => {
          console.log(id);
          e.preventDefault();
          this.setState({
              showDetails : true,
              order : this.props.list.find(o => o.orderId === id)
          })
        }
    }

    handleBackToList(){
        this.setState({
              showDetails : false
        })
    }

    formMessage(msg){
        return (<div><h1>{ msg }</h1><br /></div>); 
    }

    render(){

            if(this.props.isLoading)   return this.formMessage("Loading ...");
       else if(this.props.error)       return this.formMessage("Error ...");
       else if(this.state.showDetails){
            return <OrderDetails orderToShow={this.state.order} backToList={() => this.handleBackToList()}/>
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
                    <div>
                      <Button onClick={this.handleAdd()} variant="contained" color="primary">
                          Add New Order
                      </Button>
                      <TextField
                         label="ID"
                         name="orderId"
                         onChange={this.handleChange}
                         variant="outlined"
                      />
                      <Button onClick={e => this.props.fetchOrdersById(this.state.orderId)} variant="contained" color="primary">
                          Find
                      </Button>
                    </div>
                    <TableContainer component={Paper}>
                      <Table className={classes.table} size="small" aria-label="a dense table">
                        <TableHead>
                          <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Date</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {rows.map(row => (
                            <TableRow key={row.orderId}>
                              <TableCell component="th" scope="row">
                                {row.orderId}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {(new Date(row.orderDate)).toLocaleString()}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.orderPrice}
                              </TableCell>
                              <TableCell align="left" >
                                    <Button onClick={this.handleDetails(row.orderId)} variant="contained" color="primary">
                                        Details
                                    </Button>
                              </TableCell>
                              <TableCell align="left" >
                                    <Button onClick={this.handleDelete(row.orderId)} variant="contained" color="primary">
                                        Delete
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
    list        : state.orderSection.orderList,
    isLoading   : state.orderSection.listLoading,
    error       : state.orderSection.listError
})

//getBacklog is an action creator
//it will be wrapped inside a function with a body like : 
// () => store.dispatch(getBacklog())
//next things is that dispatched action will be caught by and executed by thunk
export default connect(mapStateToProps,{ fetchOrders,deleteOrder,fetchOrdersById })( OrderList );
