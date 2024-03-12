package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Episode;
import com.radionow.stream.model.Podcast;

import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchPodcast;

import com.radionow.stream.service.EpisodeSearchService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastSearchService;
import com.radionow.stream.service.PodcastService;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PodcastController {
	
	@Autowired
	PodcastService podcastService;
	
	@Autowired
	EpisodeService episodeService;

	@GetMapping("/podcasts")
	public ResponseEntity<List<Podcast>> getAllPodcastss(@RequestParam(required = false) String title) {
		try {
			List<Podcast> podcasts = new ArrayList<Podcast>();

			if (title == null)
				podcastService.findAll().forEach(podcasts::add);
			else
				podcastService.findByTitleContaining(title).forEach(podcasts::add);

			if (podcasts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(podcasts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/podcasts/{id}")
	public ResponseEntity<Podcast> getPodcastById(@PathVariable("id") long id) {
		Optional<Podcast> podcastData = podcastService.findById(id);

		if (podcastData.isPresent()) {
			return new ResponseEntity<>(podcastData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/podcasts")
	public ResponseEntity<Podcast> createPodcast(@RequestBody Podcast podcast) {
		try {
			Podcast _podcast = podcastService
					.save(new Podcast(
							podcast.getTitle(), 
							podcast.getAuthor(), 
							podcast.getDescription(),
							podcast.getEpisodes(), 
							podcast.getCategories(),
							podcast.getFeedURL(), 
							podcast.getArtworkURL()
							
							));
			return new ResponseEntity<>(_podcast, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/podcasts")
	public ResponseEntity<Podcast> updatePodcast(@RequestBody Podcast podcast) {
		try {
			Podcast _podcast = podcastService
					.save(podcast);
			return new ResponseEntity<>(_podcast, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/podcasts/{id}/episodes")
	public ResponseEntity<Page<Episode>> getEpisodesByPodcastId(
			@PathVariable("id") long id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "20") int size
			) {
		
		Pageable paging = PageRequest.of(page, size, Sort.by("pubDate").descending());
		Page<Episode> episodeData = episodeService.findByPodcastId(id, paging);

		System.out.println("Retrieved a page of episodes for podcast: " + id);
		if (!episodeData.isEmpty()) {
			return new ResponseEntity<>(episodeData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	

}
