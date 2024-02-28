package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.dao.StationRepository;
import com.radionow.stream.model.Station;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class StationController {

	@Autowired
	StationRepository stationRepository;

	@GetMapping("/stations")
	public ResponseEntity<List<Station>> getAllStations(@RequestParam(required = false) String title) {
		try {
			List<Station> stations = new ArrayList<Station>();

			if (title == null)
				stationRepository.findAll().forEach(stations::add);
			else
				stationRepository.findByTitleContaining(title).forEach(stations::add);

			if (stations.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(stations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/stations/{id}")
	public ResponseEntity<Station> getStationById(@PathVariable("id") long id) {
		Optional<Station> stationData = stationRepository.findById(id);

		if (stationData.isPresent()) {
			return new ResponseEntity<>(stationData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/stations")
	public ResponseEntity<Station> createStation(@RequestBody Station station) {
		try {
			Station _station = stationRepository
					.save(new Station(station.getTitle(), station.getCallsign(), station.getFrequency(), station.getDescription(), station.getUrl(),
							station.getCategories(), station.getStatistic()));
			return new ResponseEntity<>(_station, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/stations/batch")
	public ResponseEntity<String> createStationAll(@RequestBody List<Station> stationList) {
		try {
			
			stationList.forEach(station -> stationRepository.save(station));
			
			return new ResponseEntity<>("OK", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/stations/{id}")
	public ResponseEntity<Station> updateTutorial(@PathVariable("id") long id, @RequestBody Station station) {
		Optional<Station> stationData = stationRepository.findById(id);

		if (stationData.isPresent()) {
			Station _station = stationData.get();
			_station.setTitle(station.getTitle());
			_station.setDescription(station.getDescription());
			_station.setFrequency(station.getFrequency());
			_station.setCallsign(station.getCallsign());
			_station.setCategories(station.getCategories());
			_station.setStatistic(station.getStatistic());
			_station.setUrl(station.getUrl());
			return new ResponseEntity<>(stationRepository.save(_station), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/stations/{id}")
	public ResponseEntity<HttpStatus> deleteStation(@PathVariable("id") long id) {
		try {
			stationRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/stations")
	public ResponseEntity<HttpStatus> deleteAllStations() {
		try {
			stationRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/stations/callsign/{callsign}")
	public ResponseEntity<List<Station>> findByCallsign(@PathVariable("callsign") String callsign) {
		try {
			List<Station> stations = stationRepository.findByCallsignContaining(callsign.toUpperCase());

			if (stations.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(stations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
