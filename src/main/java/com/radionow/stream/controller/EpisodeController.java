package com.radionow.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.dao.EpisodeRepository;
import com.radionow.stream.model.Episode;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EpisodeController {
	
	@Autowired
	EpisodeRepository episodeRepository;
	
	@GetMapping("/episodes/{guid}")
	public ResponseEntity<Episode> getEpisodeByGuid(@PathVariable("guid") String guid) {
		try {
			 Episode episode = episodeRepository.findByGuid(guid);

			 return new ResponseEntity<>(episode, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
