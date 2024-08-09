package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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
import com.radionow.stream.service.StationService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class StationController {

	@Autowired
	StationService stationService;

	@GetMapping("/stations/categories")
	public ResponseEntity<List<Station>> getAllStationsByCategoryName(
			@RequestParam(required = true) String name, 
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "20") int size,
			@RequestParam(required = true) String sort) {
		try {
			
			List<Station> stations = new ArrayList<Station>();

			Pageable paging = PageRequest.of(page, size, Sort.by(sort).descending());
			Boolean isPublished = true;
			//stations = stationService.findByCategoriesNameAndPublished(name, paging, isPublished);
			stations = stationService.findByCategoriesNameAndCountryAndPublished(name, "US", isPublished, paging);

			System.out.println("Station category search for: " + name);
			if (stations.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(stations, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/stations")
	public ResponseEntity<List<Station>> getStationsByPage(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String range
			) {
		HttpHeaders responseHeaders = new HttpHeaders();

		int start = -1;
		int end = -1;
		String[] output = {};
		if (range != null) {
			// range [0,9]
			output = range.replace("[", "").replace("]", "").split(",");
			start = Integer.valueOf(output[0]);
			end = Integer.valueOf(output[1]);
			if (start == 0) {
				page = 0;
			}
			else {
				size = end - start + 1;
				page = start / size;
			}
		}

		Pageable paging = PageRequest.of(page, size, Sort.by("statistic.rbVotes").descending());
		Boolean isPublished = true;
		Page<Station> stationData = stationService.findAllByPublished(paging, isPublished);
		long totalResults = (stationData != null) ? stationData.getTotalElements() : 0L;
		if (start > -1) {
			responseHeaders.set("Content-Range", "stations="+start+"-"+end+"/"+totalResults);
		}
		System.out.println("Retrieved a page of stations");
		
        responseHeaders.set("Access-Control-Expose-Headers", "Content-Range");

		
		if (stationData != null) {
			//return new ResponseEntity<>(stationData.getContent(), HttpStatus.OK);
	        return ResponseEntity.ok().headers(responseHeaders).body(stationData.getContent());

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
					.save(station);
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
	
	@PutMapping("/stations/batch")
	public ResponseEntity<String> updateStationAll(@RequestBody List<Station> stationList) {
		try {
			List<Station> stations = stationList.stream()
		            .map(e -> updateStation((Station) e))
		            .collect(Collectors.toList());
			stationList.forEach(station -> stationService.saveAll(stations));
			
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
				if (stat.getViews() == null) {
					stat.setViews(1L);
				}
				else {
					stat.setViews(stat.getViews() + 1);
				}
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
			_station.setImageUrl(station.getImageUrl());
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
	
	private Station updateStation(Station station) {
		Station dbStation = stationService.findById(station.getId()).get();
		if (dbStation == null) {
			stationService.save(station);
		}
		else {
			if (station.getCallsign() != null) {
				dbStation.setCallsign(station.getCallsign());
			}
			dbStation.setCallsign(station.getCallsign());
			if (station.getCategories() != null) {
				dbStation.setCategories(station.getCategories());
			}
			if (station.getStatistic() != null) {
				dbStation.setStatistic(station.getStatistic());
			} 
			if (station.getDescription() != null) {
				dbStation.setDescription(station.getDescription());
			}
			if (station.getFrequency() != null) {
				dbStation.setFrequency(station.getFrequency());
			}
			if (station.getGuid() != null) {
				dbStation.setGuid(station.getGuid());
			}
			else {
				dbStation.setGuid(UUID.randomUUID().toString());
			}
			if (station.getTitle() != null) {
				dbStation.setTitle(station.getTitle());
			}
			if (station.getUrl() != null) {
				dbStation.setUrl(station.getUrl());
			}
			if (station.getCity() != null) {
				dbStation.setCity(station.getCity());
			}
		}
		return dbStation;
	}
}
