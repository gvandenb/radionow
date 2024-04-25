package com.radionow.stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.radionow.stream.controller.FeedController;
import com.radionow.stream.repository.CategoryRepository;
import com.radionow.stream.search.service.EpisodeSearchService;
import com.radionow.stream.search.service.PodcastSearchService;
import com.radionow.stream.service.BookService;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;
import com.radionow.stream.service.StationService;

@WebMvcTest(FeedController.class)
public class FeedControllerTests {
	
	@MockBean
	private PodcastService podcastService;
	
	@MockBean
	private EpisodeService episodeService;
	
	@MockBean
	private StationService stationService;
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	PodcastSearchService podcastSearchService;
	
	@MockBean
	EpisodeSearchService episodeSearchService;
	
	@MockBean
	CategoryRepository categoryRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldFetchFeed() throws Exception {

		String url = "https://anchor.fm/s/308e8de0/podcast/rss";
		
		mockMvc.perform(get("/api/feed/podcasts").contentType(MediaType.APPLICATION_JSON)
			.queryParam("url", url))
	        .andExpect(status().is2xxSuccessful())
	        .andDo(print());
	}
}