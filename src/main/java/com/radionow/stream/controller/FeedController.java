package com.radionow.stream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastSearchService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.util.FetchFeed;
import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.FeedInformation;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class FeedController {

	@Autowired
	PodcastService podcastService;
	
	@Autowired
	EpisodeService episodeService;
	
	@Autowired
	PodcastSearchService podcastSearchService;
	
	@Autowired
	EpisodeSearchService episodeSearchService;
	
	@GetMapping("/feed")
	public ResponseEntity<Podcast> fetchPodcasts(@RequestParam(required = true) String url) {
		
		try {
			Boolean isNewPodcast = false;
			Podcast podcast = new Podcast();
			SearchPodcast searchPodcast = new SearchPodcast();
			Podcast dbPodcast = null;

			SyndFeed feed = null;
			SyndFeed searchFeed = null;
			System.out.println("Parsing feed");

			try {
				
				feed = FetchFeed.readFeed(url);
				searchFeed = FetchFeed.readFeed(url);

				com.rometools.rome.feed.module.Module feedModule = feed.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
				System.out.println("Created feed module");
				FeedInformation feedInfo = (FeedInformation)feedModule;
				
				String author = null;
				if (feedInfo != null) {  //  itunes block exists
					feedInfo.getCategories().forEach(e -> {
						podcast.getCategories().add(new Category(e.getName()));
					});
					
					author = (feedInfo.getAuthor() == null ) ? feedInfo.getOwnerName() : feedInfo.getAuthor();
				
				}
				
				if (podcast.getCategories().isEmpty()) {
					if (!feed.getCategories().isEmpty()) {
						feed.getCategories().forEach(e -> {
							podcast.getCategories().add(new Category(e.getName()));
						});
					}
				}
				
				if (author == null) {
					author = feed.getCopyright();
				}
				System.out.println("Author: " + feedInfo.getAuthor());

				podcast.setAuthor(author);
			
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
			podcast.setTitle(feed.getTitle());
			podcast.setDescription(feed.getDescription());
			podcast.setFeedURL(url);
			podcast.setArtworkURL(feed.getImage().getUrl());
			
			// save Podcast to PosgreSQL
			try {
				System.out.println("Preparing to save podcast");
				dbPodcast = podcastService.findByTitle(podcast.getTitle());

				if (dbPodcast == null) {
					isNewPodcast = true;
					dbPodcast  = podcastService.save(podcast);
					SearchPodcast sp = createSearchPodcast(dbPodcast);
					podcastSearchService.save(sp);
				}
				else {
					// update podcast
					dbPodcast.setArtworkURL(podcast.getArtworkURL());
					dbPodcast.setAuthor(podcast.getAuthor());
					dbPodcast.setCategories(podcast.getCategories());
					dbPodcast.setDescription(podcast.getDescription());
					dbPodcast.setFeedURL(podcast.getFeedURL());
					dbPodcast.setTitle(podcast.getTitle());
					dbPodcast  = podcastService.save(dbPodcast);

				}
				
				if (dbPodcast == null) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				System.out.println("Saved podcast " + dbPodcast.getTitle());
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			// Add episodes to dataset
			try {
				final Podcast pod = dbPodcast;
				List<Episode> episodes = (List<Episode>) feed.getEntries().stream()
		            .map(e -> createEpisode((SyndEntry) e, pod))
		            .collect(Collectors.toList());
		
				System.out.println("Successfully set episodes: " + episodes.size());

			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			/*
			try {
				final Podcast dbPod = dbPodcast;
				List<SearchEpisode> searchEpisodes = (List<SearchEpisode>) searchFeed.getEntries().stream()
		            .map(e -> createSearchEpisode((SyndEntry) e, dbPod))
		            .collect(Collectors.toList());
				System.out.println("Successfully set search episodes: " + searchEpisodes.size());

				return new ResponseEntity<>(dbPod, HttpStatus.CREATED);

			}
			catch(Exception ex) {
				ex.printStackTrace();
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

			}
			*/
			return new ResponseEntity<>(dbPodcast, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/feed/rebuild/index/podcasts")
	public ResponseEntity<String> rebuildSearchPodcastIndex() {
		Long startTime = System.currentTimeMillis();
		System.out.println("Started indexing podcasts: " + startTime);
		List<Podcast> podcasts = new ArrayList<Podcast>();
		podcasts = podcastService.findAll();
		List<SearchPodcast> searchPodcasts = podcasts.stream()
	            .map(e -> createSearchPodcast((Podcast) e))
	            .collect(Collectors.toList());
		podcastSearchService.saveAll(searchPodcasts);
		Long endTime = System.currentTimeMillis();
		System.out.println("Finished indexing podcasts: " + ((startTime - endTime)/1000) + " seconds");
		String response = "Indexed " + searchPodcasts.size() + " podcasts in " + (startTime - endTime)/1000 + " seconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/feed/rebuild/index/episodes")
	public ResponseEntity<String> rebuildSearchEpisodeIndex() {
		Long startTime = System.currentTimeMillis();
		System.out.println("Started indexing episodes: " + startTime);
		List<Episode> episodes = new ArrayList<Episode>();
		episodes = episodeService.findAll();
		List<SearchEpisode> searchEpisodes = episodes.stream()
	            .map(e -> createSearchEpisode((Episode) e))
	            .collect(Collectors.toList());
		episodeSearchService.saveAll(searchEpisodes);
		Long endTime = System.currentTimeMillis();
		System.out.println("Finished indexing episodes: " + ((startTime - endTime)/1000) + " seconds");
		String response = "Indexed " + searchEpisodes.size() + " episodes in " + (startTime - endTime)/1000 + " seconds";
		return new ResponseEntity<>(response, HttpStatus.CREATED);		
	}
	
	private Episode createEpisode(SyndEntry s, Podcast podcast) {
		Episode episode = new Episode ();
		if (!s.getEnclosures().isEmpty()) {
			episode.setTitle(s.getTitle());
			episode.setPubDate(s.getPublishedDate());
			//s.getUpdatedDate()
			episode.setGuid(s.getUri());
				
			episode.setRemoteURL(((SyndEnclosure)s.getEnclosures().get(0)).getUrl());
			episode.setDescription((s.getDescription() != null) ? s.getDescription().getValue() : "");
			
			// check for itunes stanza
			com.rometools.rome.feed.module.Module entryModule = s.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
		    EntryInformation entryInfo = (EntryInformation)entryModule;
		    //episode.setDuration(entryInfo.getDuration().getMilliseconds());
		    // duration
		    Long duration = 0L; 
		    if (s.getEnclosures() != null) {
		    	SyndEnclosure se = s.getEnclosures().get(0);
		    	if (se != null) {
		    		duration = se.getLength();
		    	}
		    }
		    if (duration == null || duration == 0) {
		    	if (entryInfo.getDuration() != null) {
		    		duration = entryInfo.getDuration().getMilliseconds();
		    	}
		    }
		    	
		    episode.setDuration(duration);
			episode.setPodcast(podcast);
			Episode ep = episodeService.getEpisodeByGuid(episode.getGuid());
			if (ep == null) {
				// does not exist in db
				episodeService.save(episode);
				System.out.println("new episode guid saved: " + episode.getGuid() + " Updated.");
			}
		}
		return episode;
	}
		
	private SearchEpisode createSearchEpisode(Episode ep) {
		SearchEpisode episode = new SearchEpisode ();
		try {
			episode.setTitle(ep.getTitle());
			episode.setPubDate(ep.getPubDate());
			episode.setGuid(ep.getGuid());
			episode.setRemoteURL(ep.getRemoteURL());
			episode.setDescription(ep.getDescription());
		    episode.setDuration(ep.getDuration());
			episode.setPodcastId(ep.getPodcast().getId());
		}
		catch(Exception ex) {
			System.out.println("Episode id, guid, title: " + ep.getId() + "::" + ep.getGuid() + "::" + ep.getTitle());
			ex.printStackTrace();
		}
		return episode;
	}
	
	private SearchPodcast createSearchPodcast(Podcast podcast) {
		SearchPodcast searchPodcast = new SearchPodcast();
		searchPodcast.setAuthor(podcast.getAuthor());
		searchPodcast.setTitle(podcast.getTitle());
		searchPodcast.setDescription(podcast.getDescription());
		searchPodcast.setFeedURL(podcast.getFeedURL());
		searchPodcast.setArtworkURL(podcast.getArtworkURL());
		searchPodcast.setPodcastId(podcast.getId());
		for(Category category : podcast.getCategories()) {
			SearchCategory sc = new SearchCategory();
			sc.setName(category.getName());
			searchPodcast.getCategories().add(sc);
		}
		
		return searchPodcast;
	
	}
}
