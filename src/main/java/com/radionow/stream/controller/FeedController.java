package com.radionow.stream.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.radionow.stream.model.Station;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.search.model.SearchCategory;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchPodcast;
import com.radionow.stream.search.service.EpisodeSearchService;
import com.radionow.stream.search.service.PodcastSearchService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.service.StationService;
import com.radionow.stream.util.FetchFeed;
import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.FeedInformation;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import de.sfuhrm.radiobrowser4j.AdvancedSearch;
import de.sfuhrm.radiobrowser4j.ConnectionParams;
import de.sfuhrm.radiobrowser4j.EndpointDiscovery;
import de.sfuhrm.radiobrowser4j.FieldName;
import de.sfuhrm.radiobrowser4j.ListParameter;
import de.sfuhrm.radiobrowser4j.RadioBrowser;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class FeedController {

	@Autowired
	PodcastService podcastService;
	
	@Autowired
	EpisodeService episodeService;
	
	@Autowired
	StationService stationService;
	
	@Autowired
	PodcastSearchService podcastSearchService;
	
	@Autowired
	EpisodeSearchService episodeSearchService;
	
	@GetMapping("/feed")
	public ResponseEntity<Podcast> fetchPodcasts(@RequestParam(required = true) String url) {
		
		try {
			Podcast podcast = new Podcast();
			Podcast dbPodcast = null;

			SyndFeed feed = null;
			System.out.println("Parsing feed");

			try {
				
				feed = FetchFeed.readFeed(url);

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
				//System.out.println("Author: " + feedInfo.getAuthor());

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
					dbPodcast  = podcastService.save(podcast);
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
				podcastService.save(pod);

				System.out.println("Successfully set episodes: " + episodes.size());

			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
			return new ResponseEntity<>(dbPodcast, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/feed/stations")
	public ResponseEntity<String> fetchStations() {
		String  response = "";
		List<Station> stationList = new ArrayList<Station>();
	
		// discover endpoint
		Optional<String> endpoint = null;
		try {
			endpoint = new EndpointDiscovery("streamer/1.0").discover();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (endpoint != null) {
			// build instance
			RadioBrowser radioBrowser = new RadioBrowser(
			    ConnectionParams.builder().apiUrl(endpoint.get()).userAgent("Demo agent/1.0").timeout(50000).build());
	
			
			AdvancedSearch advancedSearch = AdvancedSearch.builder()
					.countryCode("US").build();
			
	        Stream<de.sfuhrm.radiobrowser4j.Station> stream = radioBrowser.listStationsWithAdvancedSearch(advancedSearch);
	        
	        List<Station> stations = stream
	        		//.limit(2)
		            .map(e -> updateStation((de.sfuhrm.radiobrowser4j.Station) e))
		            .collect(Collectors.toList());
	        
			response = "Inserted " + stations.size() + " US stations.\n";
			
			Stream<de.sfuhrm.radiobrowser4j.Station> topClickStream = radioBrowser.listTopClickStations().limit(5000);
			List<Station> topClickStations = topClickStream
	        		//.limit(2)
		            .map(e -> updateStation((de.sfuhrm.radiobrowser4j.Station) e))
		            .collect(Collectors.toList());
			
			response += "Inserted " + topClickStations.size() + " Top Click stations.\n";

			
			Stream<de.sfuhrm.radiobrowser4j.Station> topVoteStream = radioBrowser.listTopVoteStations().limit(5000);
			List<Station> topVoteStations = topVoteStream
	        		//.limit(2)
		            .map(e -> updateStation((de.sfuhrm.radiobrowser4j.Station) e))
		            .collect(Collectors.toList());
			
			response += "Inserted " + topVoteStations.size() + " Top Vote stations.\n";

			/*
	        Map<String, Integer> countries = radioBrowser.listCountries();

			// use click count order for getting top 100 from each country in country list
	        ListParameter listParameter = ListParameter.create();
	        listParameter.order(FieldName.CLICKCOUNT);
	        */
			
		}
		else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	private Station updateStation(de.sfuhrm.radiobrowser4j.Station s) {
		Station updatedStation = new Station();
		Station dbStation = stationService.findByGuid(s.getStationUUID().toString());
		
		if (dbStation == null) {
			Station station = new Station();

			station.setTitle(s.getName());
			station.setState(s.getState());
			station.setCountry(s.getCountryCode());
			station.setUrl(s.getUrl());
			station.setGuid(s.getStationUUID().toString());
			station.setLanguage(s.getLanguage());
			station.setImageUrl(s.getFavicon());
			
			Statistic stats = new Statistic();
			//stats.setViews(100L);
			stats.setRbClicks(s.getClickcount());	
			stats.setRbVotes(s.getVotes());
			stats.setStatisticType(StatisticType.STATION);
			station.setStatistic(stats);
			
			updatedStation = stationService.save(station);
		}
		else {
			Statistic stats = dbStation.getStatistic();
			if (stats == null) {
				stats = new Statistic();
				stats.setViews(100L);
			}
			else {
				if (stats.getViews() == null) {
					stats.setViews(100L);
				}
				else {
					stats.setViews(stats.getViews() + 100L);

				}
			}
			
			stats.setRbClicks(s.getClickcount());	
			stats.setRbVotes(s.getVotes());
			stats.setStatisticType(StatisticType.STATION);
			dbStation.setStatistic(stats);
			
			updatedStation = stationService.save(dbStation);

		}

		// TODO: make categories update idempotent
		//List<Category> categories = s.getTagList().stream().map(e -> new Category(e)).collect(Collectors.toList());
		//station.setCategories(categories);		

		return updatedStation;
	}
	
	private Episode createEpisode(SyndEntry s, Podcast podcast) {
		Episode episode = new Episode ();
		
		if (!s.getEnclosures().isEmpty()) {
			episode.setTitle(s.getTitle());
			episode.setPubDate(s.getPublishedDate());
			//s.getUpdatedDate()
			if (podcast.getLastPubDate() == null) {
				podcast.setLastPubDate(episode.getPubDate());
			}
			if (episode.getPubDate() != null && episode.getPubDate().after(podcast.getLastPubDate())) {
				podcast.setLastPubDate(episode.getPubDate());
			}
			episode.setGuid(s.getUri());
				
			episode.setRemoteURL(((SyndEnclosure)s.getEnclosures().get(0)).getUrl());
			episode.setDescription((s.getDescription() != null) ? s.getDescription().getValue() : "");
			
			// check for itunes stanza
			com.rometools.rome.feed.module.Module entryModule = s.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
		    EntryInformation entryInfo = (EntryInformation)entryModule;
		    //episode.setDuration(entryInfo.getDuration().getMilliseconds());
		    // duration
		    Long duration = 0L; 
		    if (entryInfo != null) {
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
		

}
