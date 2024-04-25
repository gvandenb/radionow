package com.radionow.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	@GetMapping("/episodes")
	public ResponseEntity<Episode> getEpisodeByGuid(@RequestParam("guid") String guid) {
		try {
			System.out.println("Guid: " + guid);
			 Episode episode = episodeService.getEpisodeByGuid(guid);

			 return new ResponseEntity<>(episode, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/episodes/views")
	public ResponseEntity<Long> fetchEpisodeViews(@RequestParam("guid") String guid) {
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
	
	@PutMapping("/episodes/views")
	public ResponseEntity<String> updateEpisodeViews(@RequestParam("guid") String guid) {
		Episode episode = episodeService.findByGuid(guid);

		if (episode != null) {
			Statistic stat = episode.getStatistic();
			if (stat != null) {
				stat.setViews(stat.getViews() + 1);
				stat.setStatisticType(StatisticType.EPISODE);
			}
			else {
				stat = new Statistic(1L, StatisticType.EPISODE);
			}
			episode.setStatistic(stat);
			episodeService.save(episode);
			
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
}
