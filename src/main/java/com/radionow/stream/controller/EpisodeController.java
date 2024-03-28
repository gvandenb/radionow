package com.radionow.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Episode;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.service.EpisodeService;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EpisodeController {
	
	@Autowired
	EpisodeService episodeService;
	
	
	@GetMapping("/episodes/{guid}")
	public ResponseEntity<Episode> getEpisodeByGuid(@PathVariable("guid") String guid) {
		try {
			 Episode episode = episodeService.getEpisodeByGuid(guid);

			 return new ResponseEntity<>(episode, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/episodes/{guid}/views")
	public ResponseEntity<Long> fetchEpisodeViews(@PathVariable("guid") String guid) {
		Episode episode = episodeService.findByGuid(guid);
		Long viewCount = 1L;
		if (episode != null) {
			if (episode.getStatistic() != null) {
				viewCount = episode.getStatistic().getViews();
			}
			
			return new ResponseEntity<>(viewCount, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/episodes/{guid}/views")
	public ResponseEntity<String> updateEpisodeViews(@PathVariable("guid") String guid) {
		Episode episode = episodeService.findByGuid(guid);

		if (episode != null) {
			Statistic stat = episode.getStatistic();
			if (stat != null) {
				stat.setViews(stat.getViews() + 1);
			}
			else {
				stat = new Statistic(1L, StatisticType.STATION);
			}
			episode.setStatistic(stat);
			episodeService.save(episode);
			
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
}
