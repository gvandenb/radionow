package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Author;
import com.radionow.stream.model.Book;
import com.radionow.stream.model.Category;
import com.radionow.stream.model.Chapter;
import com.radionow.stream.model.Episode;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.model.Station;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.search.model.SearchAudiobook;
import com.radionow.stream.search.model.SearchAudiobookResult;
import com.radionow.stream.search.model.SearchAuthor;
import com.radionow.stream.search.model.SearchCategory;
import com.radionow.stream.search.model.SearchChapter;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchEpisodeResult;
import com.radionow.stream.search.model.SearchPodcast;
import com.radionow.stream.search.model.SearchPodcastResult;
import com.radionow.stream.search.model.SearchStation;
import com.radionow.stream.search.model.SearchStationResult;
import com.radionow.stream.search.model.SearchStatistic;
import com.radionow.stream.search.model.SearchStatistic.StatisticType;
import com.radionow.stream.search.service.AudiobookSearchService;
import com.radionow.stream.search.service.EpisodeSearchService;
import com.radionow.stream.search.service.PodcastSearchService;
import com.radionow.stream.search.service.StationSearchService;
import com.radionow.stream.service.BookService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.service.StationService;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SearchController {

	@Autowired
	PodcastService podcastService;
	
	@Autowired
	EpisodeService episodeService;
	
	@Autowired
	StationService stationService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	PodcastSearchService podcastSearchService;
	
	@Autowired
	EpisodeSearchService episodeSearchService;
	
	@Autowired
	StationSearchService stationSearchService;
	
	@Autowired
	AudiobookSearchService audiobookSearchService;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
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
			se.setPodcast(updateSearchPodcast(episode.getPodcast()));
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
	
	/*
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
	*/
	@GetMapping("/search/rebuild/index/podcasts")
	public ResponseEntity<String> rebuildSearchPodcastIndex() {
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_podcasts")).delete();

		Long startTime = System.currentTimeMillis();
		List<Podcast> podcasts = new ArrayList<Podcast>();
		podcasts = podcastService.findAll();
		List<SearchPodcast> searchPodcasts = podcasts.stream()
	            .map(e -> updateSearchPodcast((Podcast) e))
	            .collect(Collectors.toList());
		podcastSearchService.saveAll(searchPodcasts);
		Long endTime = System.currentTimeMillis();
		System.out.println("Indexed podcasts: " + (endTime - startTime) + " milliseconds");
		String response = "Indexed " + searchPodcasts.size() + " podcasts in " + (endTime - startTime) + " milliseconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/search/rebuild/index/episodes")
	public ResponseEntity<String> rebuildSearchEpisodeIndexDelta() {

		Long startTime = System.currentTimeMillis();
		List<Episode> episodes = new ArrayList<Episode>();
		
		Integer page = 0;
		Integer size = 1000;
		Integer totalPages = 0;
		Long totalResults = 0L;

		
		Pageable paging = PageRequest.of(page, size);
		
		// 1. select latest episodes that have not been indexed by search (is_indexed == false) from episodes table
		Page<Episode> episodeData = episodeService.findByIsIndexed(false, paging);
		totalPages = episodeData.getTotalPages();
		totalResults = episodeData.getTotalElements();
		System.out.println("totalPages: " + totalPages);
		while(totalPages != 0) {
			episodes = episodeData.getContent();

			List<SearchEpisode> searchEpisodes = episodes.stream()
		            .map(e -> updateSearchEpisode((Episode) e))
		            .collect(Collectors.toList());
			episodeSearchService.saveAll(searchEpisodes);
			
			//page += 1;
			paging = PageRequest.of(page, size);
			episodeData = episodeService.findByIsIndexed(false, paging);
			totalPages = episodeData.getTotalPages();

		}
		
		Long endTime = System.currentTimeMillis();
		System.out.println("Indexed episodes: " + (endTime - startTime) + " milliseconds");
		String response = "Indexed " + totalResults + " episodes in " + (endTime - startTime) + " milliseconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// DELETE INDEX AND REBUILD ENTIRE EPISODE INDEX
	@GetMapping("/search/rebuild/index/episodes")
	public ResponseEntity<String> rebuildSearchEpisodeIndex() {
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_episodes")).delete();  // TODO: 

		Long startTime = System.currentTimeMillis();
		List<Episode> episodes = new ArrayList<Episode>();
		
		
		Integer page = 0;
		Integer size = 1000;
		Integer totalPages = 0;
		Long totalResults = 0L;

		
		Pageable paging = PageRequest.of(page, size, Sort.by("pubDate").descending());
		Page<Episode> episodeData = episodeService.findAll(paging);
		totalPages = episodeData.getTotalPages();
		totalResults = episodeData.getTotalElements();
		
		while(page < totalPages) {
		
			episodes = episodeData.getContent();
			List<SearchEpisode> searchEpisodes = episodes.stream()
		            .map(e -> updateSearchEpisode((Episode) e))
		            .collect(Collectors.toList());
			episodeSearchService.saveAll(searchEpisodes);
			
			page += 1;
			paging = PageRequest.of(page, size, Sort.by("pubDate").descending());
			episodeData = episodeService.findAll(paging);

		}
		
		
		Long endTime = System.currentTimeMillis();
		System.out.println("Indexed episodes: " + (endTime - startTime) + " milliseconds");
		String response = "Indexed " + totalResults + " episodes in " + (endTime - startTime) + " milliseconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);		
	}
	
	@GetMapping("/search/rebuild/index/stations")
	public ResponseEntity<String> rebuildSearchStationIndex() {
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_stations")).delete();

		Long startTime = System.currentTimeMillis();
		List<Station> stations = new ArrayList<Station>();
		
		Integer page = 0;
		Integer size = 1000;
		Integer totalPages = 0;
		Long totalResults = 0L;
		
		Pageable paging = PageRequest.of(page, size);
		Boolean isPublished = true;
		Page<Station> stationData = stationService.findAllByPublished(paging, isPublished);
		totalPages = stationData.getTotalPages();
		totalResults = stationData.getTotalElements();
		
		while(page < totalPages) {
			
			stations = stationData.getContent();
			List<SearchStation> searchStations = stations.stream()
		            .map(e -> updateSearchStation((Station) e))
		            .collect(Collectors.toList());
			stationSearchService.saveAll(searchStations);
			
			page += 1;
			paging = PageRequest.of(page, size);
			stationData = stationService.findAllByPublished(paging, isPublished);
		
		}
		Long endTime = System.currentTimeMillis();
		System.out.println("Indexed stations: " + (endTime - startTime) + " milliseconds");
		String response = "Indexed " + totalResults + " stations in " + (endTime - startTime) + " milliseconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);		
	}
	
	@GetMapping("/search/rebuild/index/audiobooks")
	public ResponseEntity<String> rebuildSearchAudiobookIndex() {
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_audiobooks")).delete();
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_authors")).delete();
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_chapters")).delete();
		elasticsearchTemplate.indexOps(IndexCoordinates.of("search_statistics")).delete();

		Long startTime = System.currentTimeMillis();
		List<Book> audiobooks = new ArrayList<Book>();
		
		Integer page = 0;
		Integer size = 1000;
		Integer totalPages = 0;
		Long totalResults = 0L;

		Pageable paging = PageRequest.of(page, size);
		Page<Book> audiobooksData = bookService.findAll(paging);
		totalPages = audiobooksData.getTotalPages();
		totalResults = audiobooksData.getTotalElements();
		
		while(page < totalPages) {
		
			audiobooks = audiobooksData.getContent();
			List<SearchAudiobook> searchAudiobooks = audiobooks.stream()
		            .map(e -> updateSearchAudiobook((Book) e))
		            .collect(Collectors.toList());
			audiobookSearchService.saveAll(searchAudiobooks);
			
			page += 1;
			paging = PageRequest.of(page, size);
			audiobooksData = bookService.findAll(paging);

		}
		
		Long endTime = System.currentTimeMillis();
		System.out.println("Indexed audiobooks: " + (endTime - startTime) + " milliseconds");
		String response = "Indexed " + totalResults + " audiobooks in " + (endTime - startTime) + " milliseconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/search/episodes")
	public ResponseEntity<SearchEpisodeResult> getEpisodeByMultiMatch(
				@RequestParam(required = true) String q,
				@RequestParam(required = false, value = "from", defaultValue = "0") Integer page,
				@RequestParam(required = false, value = "size", defaultValue = "20") Integer size			
			) {
		try {
			List<SearchEpisode> episodes = new ArrayList<SearchEpisode>();
			List<String> fields = new ArrayList<String>();
			fields.add("title");
			fields.add("description");
			fields.add("categories");
			SearchResponse<SearchEpisode> response = episodeSearchService.multiMatch(q, fields, page, size);
			List<Hit<SearchEpisode>> listOfHits = response.hits().hits();
			Long totalResults = response.hits().total().value();
			for(Hit<SearchEpisode>  hit : listOfHits){
				SearchEpisode se = hit.source();
				
				episodes.add(se);  
			}
			SearchEpisodeResult searchEpisodeResult = new SearchEpisodeResult(totalResults, episodes);

			return new ResponseEntity<>(searchEpisodeResult, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/search/stations")
	public ResponseEntity<SearchStationResult> getStationByMultiMatch(
				@RequestParam(required = true) String q,
				@RequestParam(required = false, value = "from", defaultValue = "0") Integer page,
				@RequestParam(required = false, value = "size", defaultValue = "20") Integer size			
			) {
		try {
			List<SearchStation> stations = new ArrayList<SearchStation>();
			List<String> fields = new ArrayList<String>();
			fields.add("title");
			fields.add("callsign");
			fields.add("frequency");
			fields.add("city");
			fields.add("description");
			fields.add("categories");
			SearchResponse<SearchStation> response = stationSearchService.multiMatch(q, fields, page, size);
			
			List<Hit<SearchStation>> listOfHits = response.hits().hits();
			Long totalResults = response.hits().total().value();
			for(Hit<SearchStation>  hit : listOfHits){
				SearchStation ss = hit.source();
				stations.add(ss);  
			}
			SearchStationResult searchStationResult = new SearchStationResult(totalResults, stations);
			
			return new ResponseEntity<>(searchStationResult, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/search/podcasts")
	public ResponseEntity<SearchPodcastResult> getPodcastByMultiMatch(
				@RequestParam(required = true) String q,
				@RequestParam(required = false, value = "from", defaultValue = "0") Integer page,
				@RequestParam(required = false, value = "size", defaultValue = "20") Integer size			
			) {
		try {
			List<SearchPodcast> podcasts = new ArrayList<SearchPodcast>();
			List<String> fields = new ArrayList<String>();
			fields.add("title");
			fields.add("description");
			fields.add("categories");
			SearchResponse<SearchPodcast> response = podcastSearchService.multiMatch(q, fields, page, size);
			List<Hit<SearchPodcast>> listOfHits = response.hits().hits();
			Long totalResults = response.hits().total().value();
			for(Hit<SearchPodcast>  hit : listOfHits){
				SearchPodcast sp = hit.source();
				sp.setId(sp.getPodcastId());
				podcasts.add(sp);  
			}
			SearchPodcastResult searchPodcastResult = new SearchPodcastResult(totalResults, podcasts);

			return new ResponseEntity<>(searchPodcastResult, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/search/audiobooks")
	public ResponseEntity<SearchAudiobookResult> getAudiobookByMultiMatch(
				@RequestParam(required = true) String q,
				@RequestParam(required = false, value = "from", defaultValue = "0") Integer page,
				@RequestParam(required = false, value = "size", defaultValue = "20") Integer size			
			) {
		try {
			List<SearchAudiobook> audiobooks = new ArrayList<SearchAudiobook>();
			List<String> fields = new ArrayList<String>();
			fields.add("title");
			fields.add("description");
			fields.add("categories");
			fields.add("chapters");
			SearchResponse<SearchAudiobook> response = audiobookSearchService.multiMatch(q, fields, page, size);
			List<Hit<SearchAudiobook>> listOfHits = response.hits().hits();
			Long totalResults = response.hits().total().value();
			for(Hit<SearchAudiobook>  hit : listOfHits){
				SearchAudiobook sa = hit.source();
				sa.setId(sa.getId());
				audiobooks.add(sa);  
			}
			SearchAudiobookResult searchAudiobookResult = new SearchAudiobookResult(totalResults, audiobooks);

			return new ResponseEntity<>(searchAudiobookResult, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private SearchEpisode updateSearchEpisode(Episode ep) {
		SearchEpisode episode = new SearchEpisode ();
		try {
			episode.setId(ep.getId());
			episode.setTitle(ep.getTitle());
			episode.setPubDate(ep.getPubDate());
			episode.setGuid(ep.getGuid());
			episode.setRemoteURL(ep.getRemoteURL());
			episode.setDescription(ep.getDescription());
		    episode.setDuration(ep.getDuration());
			episode.setPodcast(updateSearchPodcast(ep.getPodcast()));
			
			ep.setIsIndexed(true);
			System.out.println("Saved searchEpisode guid " + ep.getGuid() + " and set is_indexed to true");
			episodeService.save(ep);
		}
		catch(Exception ex) {
			System.out.println("Episode id, guid, title: " + ep.getId() + "::" + ep.getGuid() + "::" + ep.getTitle());
			ex.printStackTrace();
		}
		return episode;
	}
	
	private SearchPodcast updateSearchPodcast(Podcast podcast) {
		SearchPodcast searchPodcast = new SearchPodcast();
		searchPodcast.setId(podcast.getId());
		searchPodcast.setAuthor(podcast.getAuthor());
		searchPodcast.setTitle(podcast.getTitle());
		searchPodcast.setGuid(podcast.getGuid());
		searchPodcast.setRank(podcast.getRank());
		searchPodcast.setDescription(podcast.getDescription());
		searchPodcast.setFeedURL(podcast.getFeedURL());
		searchPodcast.setArtworkURL(podcast.getArtworkURL());
		searchPodcast.setPodcastId(podcast.getId());
		searchPodcast.setLastPubDate(podcast.getLastPubDate());
		for(Category category : podcast.getCategories()) {
			SearchCategory sc = new SearchCategory();
			sc.setName(category.getName());
			searchPodcast.getCategories().add(sc);
		}
		
		return searchPodcast;
	
	}
	
	private SearchStation updateSearchStation(Station station) {
		SearchStation searchStation = new SearchStation();
		searchStation.setId(station.getId());
		searchStation.setCallsign(station.getCallsign());
		searchStation.setTitle(station.getTitle());
		searchStation.setDescription(station.getDescription());
		searchStation.setCity(station.getCity());
		searchStation.setGuid(station.getGuid());
		searchStation.setFrequency(station.getFrequency());
		searchStation.setUrl(station.getUrl());
		searchStation.setState(station.getState());
		searchStation.setCountry(station.getCountry());
		searchStation.setLanguage(station.getLanguage());
		searchStation.setImageUrl(station.getImageUrl());
		searchStation.setPublished(station.getPublished());
		for(Category category : station.getCategories()) {
			SearchCategory sc = new SearchCategory();
			sc.setName(category.getName());
			searchStation.getCategories().add(sc);
		}
		
		return searchStation;
	
	}
	
	private SearchAudiobook updateSearchAudiobook(Book book) {
		SearchAudiobook searchAudiobook = new SearchAudiobook();
		searchAudiobook.setId(book.getId());
		searchAudiobook.setTitle(book.getTitle());
		searchAudiobook.setGuid(book.getGuid());
		searchAudiobook.setDescription(book.getDescription());
		searchAudiobook.setCopyrightYear(book.getCopyrightYear());
		searchAudiobook.setCreatedAt(book.getCreatedAt());
		searchAudiobook.setDownloadUrl(book.getDownloadUrl());
		searchAudiobook.setFeedUrl(book.getFeedUrl());
		searchAudiobook.setImageUrl(book.getImageUrl());
		searchAudiobook.setInfoUrl(book.getInfoUrl());
		searchAudiobook.setLanguage(book.getLanguage());
		searchAudiobook.setProjectUrl(book.getProjectUrl());
		searchAudiobook.setTotalTimeSeconds(book.getTotalTimeSeconds());
		searchAudiobook.setUpdatedAt(book.getUpdatedAt());
		
		for(Author author : book.getAuthors()) {
			SearchAuthor sa = new SearchAuthor();
			sa.setFirstName(author.getFirstName());
			sa.setLastName(author.getLastName());
			sa.setDob(author.getDob());
			sa.setDod(author.getDod());
			searchAudiobook.getAuthors().add(sa);
		}
		
		for(Category category : book.getCategories()) {
			SearchCategory sc = new SearchCategory();
			sc.setName(category.getName());
			searchAudiobook.getCategories().add(sc);
		}
		
		for(Chapter chapter : book.getChapters()) {
			SearchChapter sc = new SearchChapter();
			sc.setTitle(chapter.getTitle());
			sc.setGuid(chapter.getGuid());
			sc.setPlayUrl(chapter.getPlayUrl());
			sc.setExplicit(chapter.getExplicit());
			sc.setDuration(chapter.getDuration());
			searchAudiobook.getChapters().add(sc);
		}
		
		Statistic statistic = book.getStatistic();
		if (statistic != null) {
			SearchStatistic ss = new SearchStatistic();
			ss.setStatisticType(StatisticType.AUDIOBOOK);
			ss.setViews(statistic.getViews());
		searchAudiobook.setStatistic(null);
		}
		
		return searchAudiobook;
	
	}
}
