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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Book;
import com.radionow.stream.model.Episode;
import com.radionow.stream.model.FavoriteBook;
import com.radionow.stream.model.FavoriteEpisode;
import com.radionow.stream.model.FavoritePodcast;
import com.radionow.stream.model.FavoriteStation;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.model.Station;
import com.radionow.stream.model.User;
import com.radionow.stream.model.UserView;
import com.radionow.stream.model.ViewType;
import com.radionow.stream.repository.FavoriteBookRepository;
import com.radionow.stream.repository.FavoriteEpisodeRepository;
import com.radionow.stream.repository.FavoritePodcastRepository;
import com.radionow.stream.repository.FavoriteStationRepository;
import com.radionow.stream.search.service.UserSearchService;
import com.radionow.stream.service.BookService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.service.StationService;
import com.radionow.stream.service.UserService;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	EpisodeService episodeService;
	
	@Autowired
	StationService stationService;
	
	@Autowired
	PodcastService podcastService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	FavoriteEpisodeRepository favoriteEpisodeRepository;  // TODO change to service impl
	
	@Autowired
	FavoriteStationRepository favoriteStationRepository;  // TODO change to service impl
	
	@Autowired
	FavoritePodcastRepository favoritePodcastRepository;  // TODO change to service impl
	
	@Autowired
	FavoriteBookRepository favoriteBookRepository;  // TODO change to service impl

	@Autowired
	UserSearchService userSearchService;
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			User _user = userService
					.save(new User(user.getFirstName(), 
							user.getLastName(), 
							user.getEmail(), 
							user.getPhoneNumber(), 
							user.getStatistic(),
							user.getDevices()));
			return new ResponseEntity<>(_user, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String email) {
		try {
			List<User> users = new ArrayList<User>();

			if (email == null)
				userService.findAll().forEach(users::add);
			else
				userService.findByEmailContaining(email).forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
		try {
			 User user = userService.findById(id).get();

			 return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// TODO: make more generic, not just for searchterm
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUserById(@PathVariable("id") Long id, @RequestParam(required = true) String term) {
		try {
			 User user = userService.findById(id).get();
			 if (user.getSearchTerms() == null) {
				 List<String> searchterms = new ArrayList<String>();
				 searchterms.add(term);
				 user.setSearchTerms(searchterms);
			 }
			 else {
				 user.getSearchTerms().add(term);
			 }
			 User dbUser = userService.save(user);
			 return new ResponseEntity<>(dbUser, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Favorite Stations
	//===============================================================

	@GetMapping("/users/{id}/favorites/stations")
	public ResponseEntity<List<Station>> getFavoriteStationsByUserId(@PathVariable("id") long id) {
		List<Station> stations = new ArrayList<Station>();
		
		stations = favoriteStationRepository.findByUserId(id).stream()
					.map(e -> e.getStation()).collect(Collectors.toList());
		
		if (stations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(stations, HttpStatus.OK);	
	}
	
	@GetMapping("/users/{id}/favorites/stations/{sid}")
	public ResponseEntity<Station> setFavoriteStationUserIdStationId(@PathVariable("id") Long id, @PathVariable("sid") Long sid) {
		try {
			 FavoriteStation favorite = new FavoriteStation();
			 Station station = stationService.findById(sid).get();
			 favorite.setUserId(id);
			 favorite.setStation(station);
			 
			 FavoriteStation fs = favoriteStationRepository.save(favorite);
			 return new ResponseEntity<>(station, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/favorites/stations/{sid}")
	public ResponseEntity<String> deleteFavoriteStationUserIdStationId(@PathVariable("id") Long id, @PathVariable("sid") Long sid) {
		try {
			 
			 FavoriteStation fs = favoriteStationRepository.findByUserIdAndStationId(id, sid);
			 
			 favoriteStationRepository.delete(fs);
			 return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Favorite Audiobooks
	//===============================================================

	@GetMapping("/users/{id}/favorites/books")
	public ResponseEntity<List<Book>> getFavoriteBooksByUserId(@PathVariable("id") long id) {
		List<Book> books = new ArrayList<Book>();
		
		books = favoriteBookRepository.findByUserId(id).stream()
					.map(e -> e.getBook()).collect(Collectors.toList());
		
		if (books.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(books, HttpStatus.OK);	
	}
	
	@GetMapping("/users/{id}/favorites/books/{bid}")
	public ResponseEntity<Book> setFavoriteBookUserIdBookId(@PathVariable("id") Long id, @PathVariable("bid") Long bid) {
		try {
			 FavoriteBook favorite = new FavoriteBook();
			 Book book = bookService.findById(bid);
			 favorite.setUserId(id);
			 favorite.setBook(book);
			 
			 FavoriteBook fb = favoriteBookRepository.save(favorite);
			 return new ResponseEntity<>(book, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/favorites/books/{bid}")
	public ResponseEntity<String> deleteFavoriteAudiobookUserIdBookId(@PathVariable("id") Long id, @PathVariable("bid") Long bid) {
		try {
			 
			 FavoriteBook fb = favoriteBookRepository.findByUserIdAndBookId(id, bid);
			 
			 favoriteBookRepository.delete(fb);
			 return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Favorite Episodes
	//===============================================================
	@GetMapping("/users/{id}/favorites/episodes")
	public ResponseEntity<List<Episode>> getFavoriteEpisodesByUserId(@PathVariable("id") long id) {
		List<Episode> episodes = new ArrayList<Episode>();
		
		episodes = favoriteEpisodeRepository.findByUserId(id).stream()
					.map(e -> e.getEpisode()).collect(Collectors.toList());
		
		if (episodes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(episodes, HttpStatus.OK);	
	}
	
	@GetMapping("/users/{id}/favorites/episodes/{guid}")
	public ResponseEntity<Episode> setFavoriteEpisodeUserIdEpisodeId(@PathVariable("id") Long id, @PathVariable("guid") String guid) {
		try {
			 FavoriteEpisode favorite = new FavoriteEpisode();
			 System.out.println("Guid: " + guid);
			 String result = java.net.URLDecoder.decode(guid, StandardCharsets.UTF_8);
			 System.out.println("Guid: " + result);

			 Episode episode = episodeService.findByGuid(result);
			 favorite.setUserId(id);
			 favorite.setEpisode(episode);
			 System.out.println("Saving favorite episode: " + favorite);
			 FavoriteEpisode fe = favoriteEpisodeRepository.save(favorite);
			 return new ResponseEntity<>(episode, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/favorites/episodes/{guid}")
	public ResponseEntity<String> deleteFavoriteEpisodeUserIdEpisodeId(@PathVariable("id") Long id, @PathVariable("guid") String guid) {
		try {
			String result = java.net.URLDecoder.decode(guid, StandardCharsets.UTF_8);
			Episode episode = episodeService.findByGuid(result);
			if (episode == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			else {
				FavoriteEpisode fe = favoriteEpisodeRepository.findByUserIdAndEpisodeId(id, episode.getId());
			 
				favoriteEpisodeRepository.delete(fe);
				return new ResponseEntity<>("OK", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//===============================================================
	// Favorite Podcasts
	//===============================================================
	@GetMapping("/users/{id}/favorites/podcasts")
	public ResponseEntity<List<Podcast>> getFavoritePodcastsByUserId(@PathVariable("id") long id) {
		List<Podcast> podcasts = new ArrayList<Podcast>();
		
		podcasts = favoritePodcastRepository.findByUserId(id).stream()
					.map(e -> e.getPodcast()).collect(Collectors.toList());
		
		if (podcasts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(podcasts, HttpStatus.OK);	
	}
	
	@GetMapping("/users/{id}/favorites/podcasts/{pid}")
	public ResponseEntity<Podcast> setFavoritePodcastUserIdPodcastId(@PathVariable("id") Long id, @PathVariable("pid") Long pid) {
		
		try {
			FavoritePodcast favorite = new FavoritePodcast();
			Podcast podcast = podcastService.findById(pid).get();
			favorite.setUserId(id);
			favorite.setPodcast(podcast);
			 
			FavoritePodcast fp = favoritePodcastRepository.save(favorite);
			return new ResponseEntity<>(podcast, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/favorites/podcasts/{pid}")
	public ResponseEntity<String> deleteFavoritePodcastUserIdPodcastId(@PathVariable("id") Long id, @PathVariable("pid") Long pid) {
		try {
			 
			FavoritePodcast fp = favoritePodcastRepository.findByUserIdAndPodcastId(id, pid);
			 
			favoritePodcastRepository.delete(fp);
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}




