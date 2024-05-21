package com.radionow.stream.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Book;
import com.radionow.stream.model.Episode;
import com.radionow.stream.model.BookmarkAudiobook;
import com.radionow.stream.model.BookmarkEpisode;
import com.radionow.stream.model.BookmarkPodcast;
import com.radionow.stream.model.BookmarkStation;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.model.Station;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.service.BookService;
import com.radionow.stream.service.BookmarkAudiobookService;
import com.radionow.stream.service.BookmarkEpisodeService;
import com.radionow.stream.service.BookmarkPodcastService;
import com.radionow.stream.service.BookmarkStationService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.service.StationService;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserBookmarkController {
	
	@Autowired
	EpisodeService episodeService;
	
	@Autowired
	PodcastService podcastService;
	
	@Autowired
	StationService stationService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BookmarkEpisodeService bookmarkEpisodeService;
	
	@Autowired
	BookmarkPodcastService bookmarkPodcastService;
	
	@Autowired
	BookmarkAudiobookService bookmarkAudiobookService;
	
	@Autowired
	BookmarkStationService bookmarkStationService;
	
	
	//===============================================================
	// Bookmark Stations
	//===============================================================

	@GetMapping("/users/{id}/bookmarks/stations")
	public ResponseEntity<List<Station>> getBookmarkStationsByUserId(@PathVariable("id") long id) {
		List<Station> stations = new ArrayList<Station>();
		
		stations = bookmarkStationService.findByUserId(id).stream()
					.map(e -> e.getStation()).collect(Collectors.toList());
		
		if (stations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(stations, HttpStatus.OK);	
	}
	
	@PutMapping("/users/{id}/bookmarks/stations/{sid}")
	public ResponseEntity<Station> setBookmarkStationUserIdStationId(@PathVariable("id") Long id, @PathVariable("sid") Long sid) {
		try {
			 BookmarkStation bookmark = new BookmarkStation();
			 Station station = stationService.findById(sid).get();
			 if (station == null) {
				 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			 }
			 
			 bookmark.setUserId(id);
			 bookmark.setStation(station);
			 
			 BookmarkStation bs = bookmarkStationService.save(bookmark);
			 return new ResponseEntity<>(bs.getStation(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/bookmarks/stations/{sid}")
	public ResponseEntity<Long> deleteBookmarkStationUserIdStationId(@PathVariable("id") Long id, @PathVariable("sid") Long sid) {
		try {
			 
			BookmarkStation bs = bookmarkStationService.findByUserIdAndStationId(id, sid);
			 
			if (bs == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			bookmarkStationService.delete(bs);
			return new ResponseEntity<>(bs.getStation().getId(), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Bookmark Audiobooks
	//===============================================================

	@GetMapping("/users/{id}/bookmarks/books")
	public ResponseEntity<List<Book>> getBookmarkBooksByUserId(@PathVariable("id") long id) {
		List<Book> books = new ArrayList<Book>();
		
		books = bookmarkAudiobookService.findByUserId(id).stream()
					.map(e -> e.getBook()).collect(Collectors.toList());
		
		if (books.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(books, HttpStatus.OK);	
	}
	
	@PutMapping("/users/{id}/bookmarks/books/{bid}")
	public ResponseEntity<Book> setBookmarkBookUserIdBookId(@PathVariable("id") Long id, @PathVariable("bid") Long bid) {
		try {
			 BookmarkAudiobook bookmark = new BookmarkAudiobook();
			 Book book = bookService.findById(bid);
			 
			 if (book == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			 }
			 bookmark.setUserId(id);
			 bookmark.setBook(book);
			 
			 BookmarkAudiobook bb = bookmarkAudiobookService.save(bookmark);
			 return new ResponseEntity<>(bb.getBook(), HttpStatus.OK);
			 
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/bookmarks/books/{bid}")
	public ResponseEntity<Long> deleteBookmarkAudiobookUserIdBookId(@PathVariable("id") Long id, @PathVariable("bid") Long bid) {
		try {
			 
			BookmarkAudiobook bb = bookmarkAudiobookService.findByUserIdAndBookId(id, bid);
			 if (bb == null) {
				 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			 }
			 bookmarkAudiobookService.delete(bb);
			 return new ResponseEntity<>(bb.getBook().getId(), HttpStatus.OK);
			 
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Bookmark Episodes
	//===============================================================
	@GetMapping("/users/{id}/bookmarks/episodes")
	public ResponseEntity<List<Episode>> getBookmarkEpisodesByUserId(@PathVariable("id") long id) {
		List<Episode> episodes = new ArrayList<Episode>();
		
		episodes = bookmarkEpisodeService.findByUserId(id).stream()
					.map(e -> e.getEpisode()).collect(Collectors.toList());
		
		if (episodes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(episodes, HttpStatus.OK);	
	}
	
	@PutMapping("/users/{id}/bookmarks/episodes/{guid}")
	public ResponseEntity<Episode> setBookmarkEpisodeUserIdEpisodeId(@PathVariable("id") Long id, @PathVariable("guid") String guid) {
		try {
			 BookmarkEpisode bookmark = new BookmarkEpisode();
			 String result = java.net.URLDecoder.decode(guid, StandardCharsets.UTF_8);

			 Episode episode = episodeService.findByGuid(result);
			 if (episode == null) {
				 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			 }
			 bookmark.setUserId(id);
			 bookmark.setEpisode(episode);
			 BookmarkEpisode be = bookmarkEpisodeService.save(bookmark);
			 
			 return new ResponseEntity<>(be.getEpisode(), HttpStatus.OK);
			 
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/bookmarks/episodes/{guid}")
	public ResponseEntity<Long> deleteBookmarkEpisodeUserIdEpisodeId(@PathVariable("id") Long id, @PathVariable("guid") String guid) {
		try {
			String result = java.net.URLDecoder.decode(guid, StandardCharsets.UTF_8);
			Episode episode = episodeService.findByGuid(result);
			if (episode == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			else {
				BookmarkEpisode be = bookmarkEpisodeService.findByUserIdAndEpisodeId(id, episode.getId());
				if (be == null) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);

				}
				bookmarkEpisodeService.delete(be);
				return new ResponseEntity<>(be.getEpisode().getId(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Bookmark Podcasts
	//===============================================================
	@GetMapping("/users/{id}/bookmarks/podcasts")
	public ResponseEntity<List<Podcast>> getBookmarkPodcastsByUserId(@PathVariable("id") long id) {
		List<Podcast> podcasts = new ArrayList<Podcast>();
		
		try {
			podcasts = bookmarkPodcastService.findByUserId(id).stream()
					.map(e -> e.getPodcast()).collect(Collectors.toList());
		}
		catch (Exception ex ) {
			ex.printStackTrace();
		}
		if (podcasts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(podcasts, HttpStatus.OK);	
	}
	
	@PutMapping("/users/{id}/bookmarks/podcasts/{pid}")
	public ResponseEntity<Podcast> setBookmarkPodcastUserIdPodcastId(@PathVariable("id") Long id, @PathVariable("pid") Long pid) {
		
		try {
			BookmarkPodcast bookmark = new BookmarkPodcast();
			Podcast podcast = podcastService.findById(pid).get();
			if (podcast == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			bookmark.setUserId(id);
			bookmark.setPodcast(podcast);
			 
			BookmarkPodcast bp = bookmarkPodcastService.save(bookmark);
			return new ResponseEntity<>(bp.getPodcast(), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/bookmarks/podcasts/{pid}")
	public ResponseEntity<Long> deleteBookmarkPodcastUserIdPodcastId(@PathVariable("id") Long id, @PathVariable("pid") Long pid) {
		try {
			 
			BookmarkPodcast bp = bookmarkPodcastService.findByUserIdAndPodcastId(id, pid);
			if (bp == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			bookmarkPodcastService.delete(bp);
			return new ResponseEntity<>(bp.getPodcast().getId(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
}
