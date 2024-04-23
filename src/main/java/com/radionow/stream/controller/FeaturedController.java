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

import com.radionow.stream.model.Book;
import com.radionow.stream.model.FeaturedAudiobook;
import com.radionow.stream.model.FeaturedPodcast;
import com.radionow.stream.model.FeaturedStation;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.model.Station;
import com.radionow.stream.service.FeaturedAudiobookService;
import com.radionow.stream.service.FeaturedPodcastService;
import com.radionow.stream.service.FeaturedStationService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class FeaturedController {
	
	@Autowired
	FeaturedStationService featuredStationService;
	
	@Autowired
	FeaturedPodcastService featuredPodcastService;
	
	@Autowired
	FeaturedAudiobookService featuredAudiobookService;
	
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
	
	@PostMapping("/featured/podcasts")
	public ResponseEntity<FeaturedPodcast> createFeaturedPodcasts(@RequestBody FeaturedPodcast featuredPodcast) {
		try {
			FeaturedPodcast fp = featuredPodcastService.save(featuredPodcast);

			 return new ResponseEntity<>(fp, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/featured/podcasts")
	public ResponseEntity<List<Podcast>> getActiveFeaturedPodcasts() {
		try {
			
			List<Podcast> podcastList = new ArrayList<Podcast>();
			Date currentTime = Calendar.getInstance().getTime(); 
			List<FeaturedPodcast> featuredPodcastList = featuredPodcastService.findByStartDateTimeBeforeAndEndDateTimeAfter(currentTime);

			if (featuredPodcastList != null) {
				podcastList = featuredPodcastList.stream().map(e -> e.getPodcast()).toList();
			}
			 return new ResponseEntity<>(podcastList, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/featured/books")
	public ResponseEntity<FeaturedAudiobook> createFeaturedAudiobooks(@RequestBody FeaturedAudiobook featuredAudiobook) {
		try {
			FeaturedAudiobook fa = featuredAudiobookService.save(featuredAudiobook);

			 return new ResponseEntity<>(fa, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/featured/books")
	public ResponseEntity<List<Book>> getActiveFeaturedAudiobook() {
		try {
			
			List<Book> audiobookList = new ArrayList<Book>();
			Date currentTime = Calendar.getInstance().getTime(); 
			List<FeaturedAudiobook> featuredAudiobookList = featuredAudiobookService.findByStartDateTimeBeforeAndEndDateTimeAfter(currentTime);

			if (featuredAudiobookList != null) {
				audiobookList = featuredAudiobookList.stream().map(e -> e.getBook()).toList();
			}
			 return new ResponseEntity<>(audiobookList, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
