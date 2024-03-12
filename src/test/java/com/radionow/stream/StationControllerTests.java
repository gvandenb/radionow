package com.radionow.stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radionow.stream.controller.StationController;
import com.radionow.stream.model.Station;
import com.radionow.stream.repository.StationRepository;

@WebMvcTest(StationController.class)
public class StationControllerTests {
	@MockBean
	private StationRepository stationRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateStation() throws Exception {
		Station station = new Station("Spring Boot @WebMvcTest", "CallSign", "Frequency", "Description", "URL");

		mockMvc.perform(post("/api/stations").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(station)))
	        .andExpect(status().isCreated())
	        .andDo(print());
	}

	@Test
	void shouldReturnStation() throws Exception {
	    long id = 1L;
	    Station station = new Station(id, "Spring Boot @WebMvcTest", "CallSign", "Frequency", "Description", "URL");

		when(stationRepository.findById(id)).thenReturn(Optional.of(station));
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

		when(stationRepository.findById(id)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/stations/{id}", id))
        	.andExpect(status().isNotFound())
        	.andDo(print());
	}

	@Test
	void shouldReturnListOfStations() throws Exception {
	    List<Station> stations = new ArrayList<>(
	        Arrays.asList(new Station(1L, "Spring Boot @WebMvcTest 1", "CallSign 1", "Frequency 1", "Description 1", "URL 1"),
		        new Station(2L, "Spring Boot @WebMvcTest 2", "CallSign 2", "Frequency 2", "Description 2", "URL 2"),
		        new Station(3L, "Spring Boot @WebMvcTest 3", "CallSign 3", "Frequency 3", "Description 3", "URL 3")));
	
	    when(stationRepository.findAll()).thenReturn(stations);
	    mockMvc.perform(get("/api/stations"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.size()").value(stations.size()))
	        .andDo(print());
    }

  @Test
  void shouldReturnListOfStationsWithFilter() throws Exception {
    List<Station> stations = new ArrayList<>(
        Arrays.asList(new Station(1L, "Spring Boot @WebMvcTest 1", "CallSign 1", "Frequency 1", "Description 1", "URL 1"),
        	new Station(3L, "Spring Boot @WebMvcTest 1", "CallSign 1", "Frequency 1", "Description 1", "URL 1")));

    String title = "Boot";
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add("title", title);

    when(stationRepository.findByTitleContaining(title)).thenReturn(stations);
    mockMvc.perform(get("/api/stations").params(paramsMap))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(stations.size()))
        .andDo(print());

    stations = Collections.emptyList();

    when(stationRepository.findByTitleContaining(title)).thenReturn(stations);
    mockMvc.perform(get("/api/stations").params(paramsMap))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
  
  @Test
  void shouldReturnNoContentWhenFilter() throws Exception {
    String title = "RadioNow";
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add("title", title);
    
    List<Station> stations = Collections.emptyList();

    when(stationRepository.findByTitleContaining(title)).thenReturn(stations);
    mockMvc.perform(get("/api/stations").params(paramsMap))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void shouldUpdateStation() throws Exception {
    long id = 1L;

    Station station = new Station(id, "Spring Boot @WebMvcTest 1", "CallSign 1", "Frequency 1", "Description 1", "URL 1");
    Station updatedStation = new Station(id, "Spring Boot @WebMvcTest Updated", "CallSign Updated", "Frequency Updated", "Description 1", "URL 1");

    when(stationRepository.findById(id)).thenReturn(Optional.of(station));
    when(stationRepository.save(any(Station.class))).thenReturn(updatedStation);

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

    Station updatedStation = new Station(id, "Spring Boot @WebMvcTest Updated", "CallSign Updated", "Frequency Updated", "Description 1", "URL 1");

    when(stationRepository.findById(id)).thenReturn(Optional.empty());
    when(stationRepository.save(any(Station.class))).thenReturn(updatedStation);

    mockMvc.perform(put("/api/stations/{id}", id).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedStation)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }
  
  @Test
  void shouldDeleteStation() throws Exception {
    long id = 1L;

    doNothing().when(stationRepository).deleteById(id);
    mockMvc.perform(delete("/api/stations/{id}", id))
         .andExpect(status().isNoContent())
         .andDo(print());
  }
  
  @Test
  void shouldDeleteAllStations() throws Exception {
    doNothing().when(stationRepository).deleteAll();
    mockMvc.perform(delete("/api/stations"))
         .andExpect(status().isNoContent())
         .andDo(print());
  }
}
