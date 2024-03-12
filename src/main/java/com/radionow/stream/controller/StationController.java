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

import com.radionow.stream.model.Station;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.repository.StationRepository;
import com.radionow.stream.service.StationService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class StationController {

	@Autowired
	StationService stationService;

	@GetMapping("/stations")
	public ResponseEntity<List<Station>> getAllStations(@RequestParam(required = false) String title) {
		try {
			List<Station> stations = new ArrayList<Station>();

			if (title == null)
				stationService.findAll().forEach(stations::add);
			else
				stationService.findByTitleContaining(title).forEach(stations::add);

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
		Optional<Station> stationData = stationService.findById(id);

		if (stationData.isPresent()) {
			return new ResponseEntity<>(stationData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/stations")
	public ResponseEntity<Station> createStation(@RequestBody Station station) {
		try {
			Station _station = stationService
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
			
			stationList.forEach(station -> stationService.save(station));
			
			return new ResponseEntity<>("OK", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/stations/{id}/views")
	public ResponseEntity<Long> fetchStationViews(@PathVariable("id") long id) {
		Optional<Station> stationData = stationService.findById(id);
		Long viewCount = 1L;
		if (stationData.isPresent()) {
			Station station = stationData.get();
			if (station.getStatistic() != null) {
				viewCount = station.getStatistic().getViews();
			}
			
			return new ResponseEntity<>(viewCount, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/stations/{id}/views")
	public ResponseEntity<String> updateStationViews(@PathVariable("id") long id) {
		Optional<Station> stationData = stationService.findById(id);

		if (stationData.isPresent()) {
			Station station = stationData.get();
			Statistic stat = station.getStatistic();
			if (stat != null) {
				stat.setViews(stat.getViews() + 1);
			}
			else {
				stat = new Statistic(1L, StatisticType.STATION);
			}
			station.setStatistic(stat);
			stationService.save(station);
			
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/stations/{id}")
	public ResponseEntity<Station> updateStation(@PathVariable("id") long id, @RequestBody Station station) {
		Optional<Station> stationData = stationService.findById(id);

		if (stationData.isPresent()) {
			Station _station = stationData.get();
			_station.setTitle(station.getTitle());
			_station.setDescription(station.getDescription());
			_station.setFrequency(station.getFrequency());
			_station.setCallsign(station.getCallsign());
			_station.setCategories(station.getCategories());
			_station.setStatistic(station.getStatistic());
			_station.setUrl(station.getUrl());
			return new ResponseEntity<>(stationService.save(_station), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/stations/{id}")
	public ResponseEntity<HttpStatus> deleteStation(@PathVariable("id") long id) {
		try {
			stationService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/stations")
	public ResponseEntity<HttpStatus> deleteAllStations() {
		try {
			stationService.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/stations/callsign/{callsign}")
	public ResponseEntity<List<Station>> findByCallsign(@PathVariable("callsign") String callsign) {
		try {
			List<Station> stations = stationService.findByCallsignContaining(callsign.toUpperCase());

			if (stations.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(stations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
