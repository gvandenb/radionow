package com.radionow.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Category;
import com.radionow.stream.model.Episode;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.search.model.SearchCategory;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchPodcast;
import com.radionow.stream.service.EpisodeSearchService;
import com.radionow.stream.service.PodcastSearchService;
import com.radionow.stream.service.StationSearchService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SearchController {
	
	@Autowired
	PodcastSearchService podcastSearchService;
	
	@Autowired
	EpisodeSearchService episodeSearchService;
	
	@Autowired
	StationSearchService stationSearchService;
	
	@PostMapping("/search/podcasts")
	public ResponseEntity<SearchPodcast> createSearchPodcast(@RequestBody Podcast podcast) {
		try {
			SearchPodcast searchPodcast = new SearchPodcast();
			searchPodcast.setPodcastId(podcast.getId());
			searchPodcast.setArtworkURL(podcast.getArtworkURL());
			searchPodcast.setAuthor(podcast.getAuthor());
			for(Category category : podcast.getCategories()) {
				SearchCategory sc = new SearchCategory();
				sc.setName(category.getName());
				searchPodcast.getCategories().add(sc);
			}
			searchPodcast.setDescription(podcast.getDescription());
			searchPodcast.setFeedURL(podcast.getFeedURL());
			//searchPodcast.setEpisodes(podcast.getEpisodes());
			searchPodcast.setTitle(podcast.getTitle());
			
			SearchPodcast _podcast = podcastSearchService.save(searchPodcast);
			return new ResponseEntity<>(_podcast, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/search/episodes")
	public ResponseEntity<SearchEpisode> createSearchEpisode(@RequestBody Episode episode) {
		try {
			SearchEpisode se = new SearchEpisode();
			se.setTitle(episode.getTitle()); 
			se.setGuid(episode.getGuid());
			se.setPubDate(episode.getPubDate());
			se.setPodcastId(episode.getPodcast().getId());
			se.setDuration(episode.getDuration());
			se.setDescription(episode.getDescription());
			se.setRemoteURL(episode.getRemoteURL());
			se.setLocalFilePath(episode.getLocalFilePath());
			//se.setStatistic(episode.getStatistic());
			SearchEpisode _episode = episodeSearchService
					.save(se);
							
							
							
			return new ResponseEntity<>(_episode, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/search/podcasts")
	public ResponseEntity<Page<SearchPodcast>> getPodcastsByDescriptionContaining(
	        @RequestParam(defaultValue = "podcast") String keyword,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "20") int size
			) {
				
		Pageable paging = PageRequest.of(page, size);
		Page<SearchPodcast> searchData = podcastSearchService.findAllByDescriptionContaining(keyword, paging);

		System.out.println("Retrieved a page of search podcasts");
		if (!searchData.isEmpty()) {
			return new ResponseEntity<>(searchData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
}
