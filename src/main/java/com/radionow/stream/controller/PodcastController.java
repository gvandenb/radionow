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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.dao.EpisodeRepository;
import com.radionow.stream.dao.PodcastRepository;
import com.radionow.stream.model.Episode;
import com.radionow.stream.model.Category;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.util.FetchFeed;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.modules.itunes.FeedInformation;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEnclosure;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PodcastController {
	
	@Autowired
	PodcastRepository podcastRepository;
	
	@Autowired
	EpisodeRepository episodeRepository;

	//@SuppressWarnings("unchecked")
	@GetMapping("/feed")
	public ResponseEntity<Podcast> fetchPodcasts(@RequestParam(required = true) String url) {
		try {
			Podcast podcast = new Podcast();
			SyndFeed feed = null;
			System.out.println("Parsing feed");

			try {
				feed = FetchFeed.readFeed(url);
			

				com.rometools.rome.feed.module.Module feedModule = feed.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
				FeedInformation feedInfo = (FeedInformation)feedModule;
				
				feedInfo.getCategories().forEach(e -> {
					podcast.getCategories().add(new Category(e.getName()));
				});
				
				System.out.println("CReated feed module");
	
				System.out.println("Author: " + feedInfo.getAuthor());
				podcast.setAuthor(feedInfo.getAuthor());
			
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			System.out.println("Parsed feed");
			
			System.out.println(feed.getTitle());
			podcast.setTitle(feed.getTitle());
			
			System.out.println(feed.getDescription());
			podcast.setDescription(feed.getDescription());
			
			System.out.println(url);
			podcast.setFeedURL(url);
			
			System.out.println(feed.getImage().getUrl());
			podcast.setArtworkURL(feed.getImage().getUrl());
			
			
			try {
			
				List<Episode> episodes = (List<Episode>) feed.getEntries().stream()
		            .map(e -> createEpisode((SyndEntry) e, podcast))
		            .collect(Collectors.toList());
		
				podcast.setEpisodes(episodes);
				System.out.println("Successfully set episodes: " + episodes.size());

			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
			
			// save to PosgreSQL
			try {
				System.out.println("Preparing to save podcast");

				Podcast _podcast = podcastRepository.save(podcast);
				
				System.out.println("Saved podcast");

				if (_podcast == null) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				
				return new ResponseEntity<>(_podcast, HttpStatus.CREATED);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Episode createEpisode(SyndEntry s, Podcast podcast) {
		Episode episode = new Episode ();
		episode.setTitle(s.getTitle());
		episode.setPubDate(s.getPublishedDate());
		//s.getUpdatedDate()
		episode.setGuid(s.getUri());
		
		episode.setRemoteURL(((SyndEnclosure)s.getEnclosures().get(0)).getUrl());
		episode.setDescription(s.getDescription().getValue());
		//episode.setDuration(((SyndEnclosure)s.getEnclosures().get(0)).getLength());
		
		// check for itunes stanza
		com.rometools.rome.feed.module.Module entryModule = s.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
	    EntryInformation entryInfo = (EntryInformation)entryModule;
	    episode.setDuration(entryInfo.getDuration().getMilliseconds());
		episode.setPodcast(podcast);
		
		return episode;
	}

	@GetMapping("/podcasts")
	public ResponseEntity<List<Podcast>> getAllPodcastss(@RequestParam(required = false) String title) {
		try {
			List<Podcast> podcasts = new ArrayList<Podcast>();

			if (title == null)
				podcastRepository.findAll().forEach(podcasts::add);
			else
				podcastRepository.findByTitleContaining(title).forEach(podcasts::add);

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
		Optional<Podcast> podcastData = podcastRepository.findById(id);

		if (podcastData.isPresent()) {
			return new ResponseEntity<>(podcastData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/podcasts")
	public ResponseEntity<Podcast> createPodcast(@RequestBody Podcast podcast) {
		try {
			Podcast _podcast = podcastRepository
					.save(new Podcast(
							podcast.getTitle(), 
							podcast.getAuthor(), 
							podcast.getDescription(),
							podcast.getCategories(),
							podcast.getEpisodes(), 
							podcast.getFeedURL(), 
							podcast.getArtworkURL()
							
							));
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
		
		Optional<Podcast> podcastData = podcastRepository.findById(id);
		Podcast podcast = podcastData.get();
		
		Pageable paging = PageRequest.of(page, size, Sort.by("pubDate").descending());
		Page<Episode> episodeData = episodeRepository.findByPodcastId(id, paging);

		System.out.println("Retrieved a page of episodes for podcast: " + id);
		if (!episodeData.isEmpty()) {
			return new ResponseEntity<>(episodeData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	

}
