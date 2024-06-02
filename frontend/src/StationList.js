import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
//import './style.scss';

let PageSize = 100;
let currentPage = 0;
let totalCount = 48145;

class StationList extends Component {

    constructor(props) {
        super(props);
        this.state = {stations: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/stations?page=0&size=100')
            .then(response => response.json())
            .then(data => this.setState({stations: data}));
    }

    async remove(id) {
        await fetch(`/stations/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedStations = [...this.state.stations].filter(i => i.id !== id);
            this.setState({stations: updatedStations});
        });
    }
    
    render() {
        const {stations, isLoading} = this.state;
  

        if (isLoading) {
            return <p>Loading...</p>;
        }
    
        const stationList = stations.map(station => {
            return <tr key={station.id}>
                <td style={{whiteSpace: 'nowrap'}}>{station.title}</td>
                <td>{station.description}</td>
                <td><img src={station.imageUrl} alt="" height="75" width="75"/></td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/stations/" + station.id}>Edit</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });
    
        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/stations/new">Add Station</Button>
                    </div>
                    <h3>Stations</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Title</th>
                            <th width="40%">Description</th>
                            <th width="20%">Image</th>
                            <th width="20%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {stationList}
                        </tbody>
                    </Table>

                </Container>
            </div>
        );
    }
}
export default StationList;