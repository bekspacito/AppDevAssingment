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
    fetchUnits,
    deleteUnit,
    resetDidDelete 
} from "../../actions/unitActions"
import {
    UNIT_ADD_PATH,
    UNIT_UPD_PATH,
} from "../../utils"

class UnitsList extends Component{
    constructor(props){
        super(props);
        this.onDelete = this.onDelete.bind(this)
        this.formMessage = this.formMessage.bind(this);
    }

    componentDidMount(){
        //async api call to get all the tasks
        this.props.fetchUnits();
    }

    componentDidUpdate(prevProps){
        if(this.props.didDelete){
            this.props.resetDidDelete();
            this.props.fetchUnits();
        }
    }

    onDelete(unitId){
        return e => {
            e.preventDefault();
            this.props.deleteUnit(unitId,this.props.history);
        }
    }

    formMessage(msg){
        return (<div><h1>{ msg }</h1><br /></div>); 
    }

    render(){

            if(this.props.isLoading) return this.formMessage("Loading ...");
       else if(this.props.error)     return this.formMessage("Error ...");
       else {
            const rows = this.props.units;//.map(u => <UnitsListItem history={this.props.history} unit={u} onDelete={this.onDelete(u.id)}/>)
            const classes = makeStyles({
                  table: {
                    minWidth: 650,
                  },
            });
            
            return (
                <div>
                    <Button onClick={e => this.props.history.push(`${UNIT_ADD_PATH}`)} variant="contained" color="primary">
                        Add New Unit
                    </Button>
                    <TableContainer component={Paper}>
                      <Table className={classes.table} size="small" aria-label="a dense table">
                        <TableHead>
                          <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell align="right">Update</TableCell>
                            <TableCell align="right">Delete</TableCell>
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
                                    <Button onClick={e => this.props.history.push(`${UNIT_UPD_PATH}/${row.id}`)} variant="contained" color="primary">
                                        Update
                                    </Button>
                              </TableCell>
                              <TableCell align="right">
                                    <Button onClick={this.onDelete(row.id)} variant="contained" color="primary">
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
    units       : state.unitSection.unitList,
    isLoading   : state.unitSection.listLoading,
    error       : state.unitSection.listError,
    deleting    : state.unitSection.deleting,
    deleteError : state.unitSection.deleteError,
    didDelete   : state.unitSection.didDelete
})

//getBacklog is an action creator
//it will be wrapped inside a function with a body like : 
// () => store.dispatch(getBacklog())
//next things is that dispatched action will be caught by and executed by thunk
export default connect(mapStateToProps,{ fetchUnits,deleteUnit,resetDidDelete })(UnitsList);
