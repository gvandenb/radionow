package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.radionow.stream.client.PodcastClient;
import com.radionow.stream.data.PodcastGraphqlDto;
import com.radionow.stream.data.PodcastDto;
import com.radionow.stream.model.Episode;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PodcastController {
	
	@Autowired
	PodcastService podcastService;
	
	@Autowired
	EpisodeService episodeService;
	
	@Autowired
	PodcastClient podcastClient;

	@GetMapping("/podcasts/categories")
	public ResponseEntity<List<Podcast>> getAllPodcastsByCategoryName(@RequestParam(required = true) String name, 
																	  @RequestParam(defaultValue = "0") int page,
															          @RequestParam(defaultValue = "10") int size,
															          @RequestParam(defaultValue = "DESC") String sort) {
		try {
			List<Podcast> podcasts = new ArrayList<Podcast>();

			Pageable paging = PageRequest.of(page, size);
			if (sort.equals("lastPubDate")) {
				podcasts = podcastService.findByCategoriesNameOrderByLastPubDateDesc(name, paging);
			}
			else {
				podcasts = podcastService.findByCategoriesNameOrderByRankAsc(name, paging);
			}
			
			System.out.println("Podcast category search for: " + name + " [" + page +":"+ size + "]");
			if (podcasts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(podcasts, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/podcasts")
	public ResponseEntity<List<Podcast>> getAllPodcastss(
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
		try {

			Pageable paging = PageRequest.of(page, size, Sort.by("rank").ascending());
			//Pageable paging = PageRequest.of(page, size, Sort.by("lastPubDate").descending());
			Page<Podcast> podcastData = podcastService.findAll(paging);

			if (podcastData.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(podcastData.getContent(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	@DeleteMapping("/podcasts/{id}")
	public ResponseEntity<String> deletePodcastById(@PathVariable("id") long id) {
		Optional<Podcast> podcastData = podcastService.findById(id);

		if (podcastData.isPresent()) {
			podcastService.deletePodcastById(id);
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/podcasts")
	public ResponseEntity<Podcast> createPodcast(@RequestBody Podcast podcast) {
		try {
			Podcast _podcast = podcastService.save(podcast);
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
	
	@GetMapping("/podcasts/{id}/views")
	public ResponseEntity<Long> fetchPodcastViews(@PathVariable("id") long id) {
		Optional<Podcast> podcastData = podcastService.findById(id);
		Long viewCount = 1L;
		if (podcastData.isPresent()) {
			Podcast podcast = podcastData.get();
			if (podcast.getStatistic() != null) {
				viewCount = podcast.getStatistic().getViews();
			}
			
			return new ResponseEntity<>(viewCount, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/podcasts/{id}/views")
	public ResponseEntity<String> updatePodcastViews(@PathVariable("id") long id) {
		Optional<Podcast> podcastData = podcastService.findById(id);

		if (podcastData.isPresent()) {
			Podcast podcast = podcastData.get();
			Statistic stat = podcast.getStatistic();
			if (stat != null) {
				stat.setViews(stat.getViews() + 1);
				stat.setStatisticType(StatisticType.PODCAST);
			}
			else {
				stat = new Statistic(1L, StatisticType.PODCAST);
			}
			podcast.setStatistic(stat);
			podcastService.save(podcast);
			
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/podcasts/charts")
	public ResponseEntity<List<PodcastDto>> getTopChartsByGenres(
			@RequestParam(defaultValue = "PODCASTSERIES_TRUE_CRIME") String genre,
			@RequestParam(defaultValue = "1") Integer page,
	        @RequestParam(defaultValue = "25") Integer size) {

		/*
		 * 1. get 25 results (paginated) from taddy.org (page:1, limit:25)
		 * 2. update radionow postgresql
		 * 3. adjust Apple podcast rank (either category rank or podcast rank??)
		 *  
		 */
		
		try {
			PodcastGraphqlDto podcastData = podcastClient.getTopChartsByGenres(genre, page, size);
			List<PodcastDto> podcastList = podcastData.getData().getGetTopChartsByGenres().getPodcastSeries();
			return new ResponseEntity<>(podcastList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
