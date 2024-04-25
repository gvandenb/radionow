package com.radionow.stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.radionow.stream.controller.PodcastController;
import com.radionow.stream.repository.PodcastRepository;
import com.radionow.stream.repository.StationRepository;

@WebMvcTest(PodcastController.class)
public class PodcastControllerTests {
	@MockBean
	private StationRepository stationRepository;
	
	@MockBean
	private PodcastRepository podcastRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldFetchFeed() throws Exception {

		String url = "https://anchor.fm/s/308e8de0/podcast/rss";
		
		
		mockMvc.perform(get("/api/feed").contentType(MediaType.APPLICATION_JSON)
			.queryParam("url", url))
	        .andExpect(status().is2xxSuccessful())
	        .andDo(print());
	}
}