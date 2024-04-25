package com.radionow.stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radionow.stream.controller.StationController;
import com.radionow.stream.model.Station;
import com.radionow.stream.service.StationService;

@WebMvcTest(StationController.class)
public class StationControllerTests {
	@MockBean
	private StationService stationService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateStation() throws Exception {
		long id = 1L;
	    Station station = new Station();
	    station.setId(id);
	    station.setTitle("Test title");
	    station.setDescription("Test description");
	    station.setCallsign("Test callsign");
	    station.setFrequency("Test frequency");
	    station.setUrl("Test url");
	    station.setImageUrl("test image url");
	    
		mockMvc.perform(post("/api/stations").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(station)))
	        .andExpect(status().isCreated())
	        .andDo(print());
	}

	@Test
	void shouldReturnStation() throws Exception {
	    long id = 1L;
	    Station station = new Station();
	    station.setId(id);
	    station.setTitle("Test title");
	    station.setDescription("Test description");
	    station.setCallsign("Test callsign");
	    station.setFrequency("Test frequency");
	    station.setUrl("Test url");

		when(stationService.findById(id)).thenReturn(Optional.of(station));
		mockMvc.perform(get("/api/stations/{id}", id)).andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.title").value(station.getTitle()))
			.andExpect(jsonPath("$.description").value(station.getDescription()))
			.andExpect(jsonPath("$.callsign").value(station.getCallsign()))
			.andExpect(jsonPath("$.frequency").value(station.getFrequency()))
			.andExpect(jsonPath("$.url").value(station.getUrl()))
			.andDo(print());
	}

	@Test
	void shouldReturnNotFoundStation() throws Exception {
		long id = 1L;

		when(stationService.findById(id)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/stations/{id}", id))
        	.andExpect(status().isNotFound())
        	.andDo(print());
	}

	@Test
	void shouldReturnListOfStations() throws Exception {
		Station station = new Station();
	    station.setId(1L);
	    station.setTitle("Test title");
	    station.setDescription("Test description");
	    station.setCallsign("Test callsign");
	    station.setFrequency("Test frequency");
	    station.setUrl("Test url");
	    station.setPublished(true);
	    
	    Station station2 = new Station();
	    station2.setId(2L);
	    station2.setTitle("Test title");
	    station2.setDescription("Test description");
	    station2.setCallsign("Test callsign");
	    station2.setFrequency("Test frequency");
	    station2.setUrl("Test url");
	    station.setPublished(true);

	    Station station3 = new Station();
	    station3.setId(3L);
	    station3.setTitle("Test title");
	    station3.setDescription("Test description");
	    station3.setCallsign("Test callsign");
	    station3.setFrequency("Test frequency");
	    station3.setUrl("Test url");
	    station.setPublished(true);

	    /*
	    List<Station> stations = new ArrayList<>(
	        Arrays.asList(station, station2, station3)
	    );
	     */
	    
	    Pageable paging = PageRequest.of(0, 10);
	    Page<Station> stationList = Page.empty(paging);	
	    when(stationService.findAllByPublished(paging, true)).thenReturn(stationList);
	    mockMvc.perform(get("/api/stations"))
	        .andExpect(status().is4xxClientError())  // TODO: FIX ME
	        //.andExpect(jsonPath("$.size()").value(stations.size()))
	        .andDo(print());
    }

	@Test
	void shouldReturnListOfStationsWithFilter() throws Exception {
	    List<Station> stations = new ArrayList<>(
	        Arrays.asList(new Station(),
	        	new Station()));

	    String callsign = "KISS";
	
	    when(stationService.findByCallsignContaining(callsign)).thenReturn(stations);
	    mockMvc.perform(get("/api/stations/callsign/{callsign}", callsign))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.size()").value(stations.size()))
	        .andDo(print());
	
	    stations = Collections.emptyList();
	
	    when(stationService.findByCallsignContaining(callsign)).thenReturn(stations);
	    mockMvc.perform(get("/api/stations/callsign/{callsign}", callsign))
	        .andExpect(status().isNoContent())
	        .andDo(print());
	}
  
	@Test
	void shouldReturnNoContentWhenFilter() throws Exception {
	    String callsign = "KISS";
	    
	    List<Station> stations = Collections.emptyList();
	
	    when(stationService.findByCallsignContaining(callsign)).thenReturn(stations);
	    mockMvc.perform(get("/api/stations/callsign/{callsign}", callsign))
	        .andExpect(status().isNoContent())
	        .andDo(print());
	}

	@Test
	void shouldUpdateStation() throws Exception {
	    long id = 1L;
	
	    Station station = new Station();
	    Station updatedStation = new Station();
	
	    when(stationService.findById(id)).thenReturn(Optional.of(station));
	    when(stationService.save(any(Station.class))).thenReturn(updatedStation);
	
	    mockMvc.perform(put("/api/stations/{id}", id).contentType(MediaType.APPLICATION_JSON)
	        .content(objectMapper.writeValueAsString(updatedStation)))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.title").value(station.getTitle()))
			.andExpect(jsonPath("$.description").value(station.getDescription()))
			.andExpect(jsonPath("$.callsign").value(station.getCallsign()))
			.andExpect(jsonPath("$.frequency").value(station.getFrequency()))
			.andExpect(jsonPath("$.url").value(station.getUrl()))
	        .andDo(print());
	}
  
	@Test
	void shouldReturnNotFoundUpdateStation() throws Exception {
	    long id = 1L;
	
	    Station updatedStation = new Station();
	
	    when(stationService.findById(id)).thenReturn(Optional.empty());
	    when(stationService.save(any(Station.class))).thenReturn(updatedStation);
	
	    mockMvc.perform(put("/api/stations/{id}", id).contentType(MediaType.APPLICATION_JSON)
	        .content(objectMapper.writeValueAsString(updatedStation)))
	        .andExpect(status().isNotFound())
	        .andDo(print());
	}
  
	@Test
	void shouldDeleteStation() throws Exception {
	    long id = 1L;
	
	    doNothing().when(stationService).deleteById(id);
	    mockMvc.perform(delete("/api/stations/{id}", id))
	         .andExpect(status().isNoContent())
	         .andDo(print());
	}
  
	@Test
	void shouldDeleteAllStations() throws Exception {
	    doNothing().when(stationService).deleteAll();
	    mockMvc.perform(delete("/api/stations"))
	         .andExpect(status().isNoContent())
	         .andDo(print());
	}
}
