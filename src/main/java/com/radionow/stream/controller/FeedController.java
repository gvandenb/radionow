package com.radionow.stream.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.repository.CategoryRepository;
import com.radionow.stream.search.model.SearchCategory;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchPodcast;
import com.radionow.stream.search.service.EpisodeSearchService;
import com.radionow.stream.search.service.PodcastSearchService;
import com.radionow.stream.service.BookService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.service.StationService;
import com.radionow.stream.util.FetchFeed;
import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.FeedInformation;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

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
	BookService bookService;
	
	@Autowired
	PodcastSearchService podcastSearchService;
	
	@Autowired
	EpisodeSearchService episodeSearchService;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	
	@PostMapping("/feed/books")
	public ResponseEntity<String> createBooks(@RequestBody(required = true) List<Book> books) {
		
		try {
			
			bookService.saveAll(books);
			return new ResponseEntity<>("OK", HttpStatus.CREATED);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@GetMapping("/feed/books/batch")
	public ResponseEntity<String> updateBooks() {
		
		String result = "";
		for (int i=200; i<207; i++) {
			Pageable paging = PageRequest.of(i, 100);
	
			List<Book> bookList = bookService.findAll(paging).getContent();
			List<Book> updatedBookList = bookList.stream()
					.map(e -> processBook(e.getFeedUrl(), e))
					.filter(n -> n!=null)
		            .collect(Collectors.toList());
	
			//bookService.saveAll(updatedBookList);
			System.out.println("Updated page " + i + " size 100... " + updatedBookList.size() );
		}
		result = "Update of books completed.";

		return new ResponseEntity<>(result, HttpStatus.CREATED);

	}
	
	@GetMapping("/feed/books")
	public ResponseEntity<Book> fetchBooks(@RequestParam(required = false) String url) {
		
		
		try {
			// find book by feed url
			Book existingBook = bookService.findByFeedUrl(url);
			if (existingBook == null) {
				existingBook = new Book();
			}
			Book updatedBook = processBook(url, existingBook);  // TODO: fix this
			return new ResponseEntity<>(updatedBook, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/feed/podcasts/modified")
	public ResponseEntity<List<String>> fetchPodcastsModified() {
		
		List<String> feedUrlList = podcastService.findAllFeedurl();
		feedUrlList.forEach(url -> {
	
			Podcast podcast = new Podcast();
			Podcast dbPodcast = null;

			SyndFeed feed = null;
			System.out.println("Parsing feed: " + url);

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
				
					if (feed.getImage() == null) {
						podcast.setArtworkURL(feedInfo.getImageUri());
					}
					else {
						podcast.setArtworkURL(feed.getImage().getUrl());
					}
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
				podcast.setTitle(feed.getTitle());
				podcast.setDescription(feed.getDescription());
				podcast.setFeedURL(url);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
			
			if (feed != null) {
				// save Podcast to PosgreSQL
				try {
					System.out.println("Preparing to save podcast");
					dbPodcast = podcastService.findByFeedURL(podcast.getFeedURL());  // TODO: findByFeedUrl
	
					if (dbPodcast == null) {
						dbPodcast  = podcastService.save(podcast);
					}
					else {
						// update podcast
						if (dbPodcast.getArtworkURL() == null) dbPodcast.setArtworkURL(podcast.getArtworkURL());
						if (dbPodcast.getAuthor() == null) dbPodcast.setAuthor(podcast.getAuthor());
						if (dbPodcast.getCategories() == null || dbPodcast.getCategories().isEmpty()) dbPodcast.setCategories(podcast.getCategories());
						if (dbPodcast.getDescription() == null) dbPodcast.setDescription(podcast.getDescription());
						if (dbPodcast.getTitle() == null) dbPodcast.setTitle(podcast.getTitle());
						dbPodcast  = podcastService.save(dbPodcast);
	
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				// Add latest episodes to database
				Integer updatedEpisodeCount = 0;
				try {
					Date lastPubDate = dbPodcast.getLastPubDate();
					final Podcast pod = dbPodcast;
					for (SyndEntry e : feed.getEntries()) {
						if (e.getPublishedDate().after(lastPubDate)) {
							Episode ep = createEpisode((SyndEntry) e, pod);
							updatedEpisodeCount++;
						}
						else {
							break;
						}
					}
					podcastService.save(pod);
	
					System.out.println("Successfully set episodes: " + updatedEpisodeCount);
	
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
		});
		
		return new ResponseEntity<>(feedUrlList, HttpStatus.CREATED);
	}

	@GetMapping("/feed/podcasts")
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
					if (feed.getImage() == null) {
						podcast.setArtworkURL(feedInfo.getImageUri());
					}
					else {
						podcast.setArtworkURL(feed.getImage().getUrl());
					}

				}
				System.out.println("Created feed module");

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
				podcast.setTitle(feed.getTitle());
				podcast.setDescription(feed.getDescription());
				podcast.setFeedURL(url);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
			
			
			// save Podcast to PosgreSQL
			try {
				System.out.println("Preparing to save podcast");
				dbPodcast = podcastService.findByFeedURL(podcast.getFeedURL());  // TODO: findByFeedUrl

				if (dbPodcast == null) {
					dbPodcast  = podcastService.save(podcast);
				}
				else {
					// update podcast
					if (dbPodcast.getArtworkURL() == null) dbPodcast.setArtworkURL(podcast.getArtworkURL());
					if (dbPodcast.getAuthor() == null) dbPodcast.setAuthor(podcast.getAuthor());
					if (dbPodcast.getCategories() == null || dbPodcast.getCategories().isEmpty()) dbPodcast.setCategories(podcast.getCategories());
					if (dbPodcast.getDescription() == null) dbPodcast.setDescription(podcast.getDescription());
					if (dbPodcast.getTitle() == null) dbPodcast.setTitle(podcast.getTitle());
					dbPodcast  = podcastService.save(dbPodcast);

				}
				
				if (dbPodcast == null) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				System.out.println("Saved podcast " + dbPodcast.getTitle());
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			// Add all episodes to database
			Integer updatedEpisodeCount = 0;
			try {
				
				final Podcast pod = dbPodcast;	
				List<Episode> episodes = (List<Episode>) feed.getEntries().stream()
		            .map(e -> createEpisode((SyndEntry) e, pod))
		            .collect(Collectors.toList());
				podcastService.save(pod);
				
				System.out.println("Successfully set episodes: " + updatedEpisodeCount);

			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
			return new ResponseEntity<>(dbPodcast, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
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
	
	private Book processBook(String url, Book book) {

		SyndFeed feed = null;
		System.out.println("Parsing audiobook feed");

		try {
			//url = "https://librivox.org/rss/3789";
			try {
				feed = FetchFeed.readFeed(url);
				book.setInfoUrl(feed.getLink());
				book.setLanguage(feed.getLanguage());
				book.setDescription(feed.getDescription());
			}
			catch (FeedException fex) {
				System.out.println("Feed URL: " + url);
				System.out.println(fex.getMessage());
				return null;
			}

			com.rometools.rome.feed.module.Module feedModule = feed.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
			System.out.println("Created feed module");
			FeedInformation feedInfo = (FeedInformation)feedModule;
			
			if (feedInfo != null) {  //  itunes block exists
				feedInfo.getCategories().forEach(e -> {
					System.out.println("Category Name: " + e.getName());
					Category dbCat = categoryRepository.findByName(e.getName(), Limit.of(1));
					if (dbCat != null) {
						book.getCategories().add(dbCat);
					}
					else {
						book.getCategories().add(new Category(e.getName()));
					}
				});
				book.setImageUrl(feedInfo.getImageUri());
				System.out.println("itunes:image: " + feedInfo.getImageUri());
	
			}
			
			
			if (book.getCategories().isEmpty()) {
				if (!feed.getCategories().isEmpty()) {
					feed.getCategories().forEach(e -> {
						Category dbCat = categoryRepository.findByName(e.getName(), Limit.of(1));
						if (dbCat != null) {
							book.getCategories().add(dbCat);
						}
						else {
							book.getCategories().add(new Category(e.getName()));
						}						
					});
				}
			}
			
			// Uncle Vanya by Anton Chekhov (1860 - 1904)
			if (book.getAuthors().isEmpty()) {
				Author author = new Author();
				
				String temp[] = feed.getTitle().split(" by ");
				if (temp.length > 1) {
					
					String authorName = temp[1];
					System.out.println("Parsing title: " + authorName);
					// authorName == "Anton Chekhov (1860 - 1904)"
					String[] names = authorName.split(" \\(");

					// names[0] == "Anton Chekhov"
					int lastIndexOfSpace = names[0].lastIndexOf(" ");
					if (lastIndexOfSpace > -1 && lastIndexOfSpace < names[0].length()) {
						String firstName = names[0].substring(0,lastIndexOfSpace);
						String lastName = names[0].substring(lastIndexOfSpace + 1);
						author.setFirstName(firstName);
						author.setLastName(lastName);
					}

					// names[1] == "1860 - 1904)"
					if (names.length > 1) {
						String[] years = names[1].replace(")", "").split(" - ");
						if (years.length > 0) {
							String dob = years[0];
							author.setDob(dob);
						}
						if (years.length > 1) {
							String dod = years[1];
							author.setDod(dod);
						}
					}
					
					book.getAuthors().add(author);
				}
			}
		
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		// Add chapters to book 
		Book updatedBook = new Book();
		try {
			//final Book b = book;
			List<Chapter> chapters = (List<Chapter>) feed.getEntries().stream()
	            .map(e -> createChapter((SyndEntry) e))
	            .collect(Collectors.toList());
			
			if (book.getChapters() == null || book.getChapters().isEmpty()) {
				book.setChapters(chapters);
			}
			updatedBook = bookService.save(book);

			
			//System.out.println("Successfully parsed book: " + updatedBook);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return updatedBook;
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
		
	private Chapter createChapter(SyndEntry s) {
		Chapter chapter = new Chapter ();
		
		if (!s.getEnclosures().isEmpty()) {
			chapter.setTitle(s.getTitle());
			chapter.setPlayUrl(((SyndEnclosure)s.getEnclosures().get(0)).getUrl());

			chapter.setGuid(UUID.randomUUID().toString());
			
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
		    	chapter.setExplicit(entryInfo.getExplicit());
		    }
		    	
		    chapter.setDuration(duration);
		    
		    //chapter.setBook(book);
		    
		}
		return chapter;
	}

}
