import "./App.css";
import ReactPaginate from "react-paginate";
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { useEffect, useState } from "react";

function App() {
  const [items, setItems] = useState([]);
  const [previewStation, setPreviewStation] = useState(0);
  const [pageCount, setpageCount] = useState(0);
  const [curPageIndex, setCurPageIndex] = useState(0);
  let limit = 100;

  useEffect(() => {
    const getStations = async () => {
      const res = await fetch(
        `http://localhost:8081/api/stations?page=0&size=${limit}`
      );
      const data = await res.json();
      const total = 48145;
      setpageCount(Math.ceil(total / limit));
      // console.log(Math.ceil(total/12));
      setItems(data);
    };

    getStations();
  }, [limit]);

  const fetchStations = async (currentPage) => {
    const res = await fetch(
      `http://localhost:8081/api/stations?page=${currentPage}&size=${limit}`
    );
    const data = await res.json();
    return data;
  };

  const handlePageClick = async (data) => {
    console.log(data.selected);

    let currentPage = data.selected;
    const stationsFromServer = await fetchStations(currentPage);
    setCurPageIndex(data.selected);
    setItems(stationsFromServer);
    // scroll to the top
    //window.scrollTo(0, 0)
  };

  const stationList = items.map(station => {
    return <tr key={station.id}>
        <td>{station.id}</td>
        <td>{station.title}</td>
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

    <div className="container">
    <AppNavbar/>
      <div>
          <Container fluid>
              <div style={{float: 'right'}} className="float-right">
                <ReactPaginate
                  previousLabel={"previous"}
                  nextLabel={"next"}
                  breakLabel={"..."}
                  pageCount={pageCount}
                  marginPagesDisplayed={2}
                  pageRangeDisplayed={3}
                  onPageChange={handlePageClick}
                  containerClassName={"pagination justify-content-center"}
                  pageClassName={"page-item"}
                  pageLinkClassName={"page-link"}
                  previousClassName={"page-item"}
                  previousLinkClassName={"page-link"}
                  nextClassName={"page-item"}
                  nextLinkClassName={"page-link"}
                  breakClassName={"page-item"}
                  breakLinkClassName={"page-link"}
                  activeClassName={"active"}
                />              
              </div>
              <div style={{float: 'right'}}>
                {previewStation}
              </div>
              <div style={{width: 1280 + 'px'}}>
              <h3>Stations ({curPageIndex * 100 + 1} - {curPageIndex * 100 + 100} of 48145)</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="5%">Id</th>
                        <th width="20%">Title</th>
                        <th width="40%">Description</th>
                        <th width="5%">Image</th>
                        <th width="10%">Action</th>
                        <th width="30%">Preview</th>

                    </tr>
                    </thead>
                    <tbody>
                    {stationList}
                    </tbody>
                </Table>
              </div>
              
          </Container>
      </div>

      
    </div>
  );
}

export default App;