package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.FeaturedStation;
import com.radionow.stream.model.Station;

import com.radionow.stream.service.FeaturedStationService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class FeaturedController {
	
	@Autowired
	FeaturedStationService featuredStationService;
	
	@PostMapping("/featured/stations")
	public ResponseEntity<FeaturedStation> createFeaturedStation(@RequestBody FeaturedStation featuredStation) {
		try {
			 FeaturedStation fs = featuredStationService.save(featuredStation);

			 return new ResponseEntity<>(fs, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/featured/stations")
	public ResponseEntity<List<Station>> getActiveFeaturedStations() {
		try {
			
			List<Station> stationList = new ArrayList<Station>();
			Date currentTime = Calendar.getInstance().getTime(); 
			List<FeaturedStation> featuredStationList = featuredStationService.findByStartDateTimeBeforeAndEndDateTimeAfter(currentTime);

			if (featuredStationList != null) {
				stationList = featuredStationList.stream().map(e -> e.getStation()).toList();
			}
			 return new ResponseEntity<>(stationList, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
