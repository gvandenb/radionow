package com.radionow.stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radionow.stream.controller.PodcastController;
import com.radionow.stream.model.Podcast;
import com.radionow.stream.service.EpisodeService;
import com.radionow.stream.service.PodcastService;

@WebMvcTest(PodcastController.class)
public class PodcastControllerTests {
	
	@MockBean
	private PodcastService podcastService;
	
	@MockBean
	private EpisodeService episodeService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
    Date date = new Date();

    /*
	@Test
	void shouldCreatePodcast() throws Exception {
		long id = 1L;
	    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	    Date convertedDate = (Date) formatter.parse(formatter.format(date));

	    Podcast podcast = new Podcast();
	    podcast.setId(id);
	    podcast.setTitle("Test title");
	    podcast.setDescription("Test description");
	    podcast.setGuid(UUID.randomUUID().toString());
	    podcast.setAuthor("Test author");
	    podcast.setFeedURL("Test feed url");
	    podcast.setArtworkURL("test artwork url");
	    podcast.setRank(1);
	    podcast.setLastPubDate(convertedDate);
	    podcast.setCreatedAt(convertedDate);
	    podcast.setUpdatedAt(convertedDate);
	    
		mockMvc.perform(post("/api/podcasts").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(podcast)))
	        .andExpect(status().isCreated())
	        .andDo(print());
	}

	@Test
	void shouldReturnPodcast() throws Exception {
	    long id = 1L;
	    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	    Date convertedDate = (Date) formatter.parse(formatter.format(date));
	    Podcast podcast = new Podcast();
	    podcast.setId(id);
	    podcast.setTitle("Test title");
	    podcast.setDescription("Test description");
	    podcast.setGuid(UUID.randomUUID().toString());
	    podcast.setAuthor("Test author");
	    podcast.setFeedURL("Test feed url");
	    podcast.setArtworkURL("test artwork url");
	    podcast.setRank(1);
	    podcast.setLastPubDate(convertedDate);
	    podcast.setCreatedAt(convertedDate);
	    podcast.setUpdatedAt(convertedDate);
	    
	    System.out.println(formatter.format(convertedDate));
	    //System.out.println(jsonPath("$.lastPubDate").value());

		when(podcastService.findById(id)).thenReturn(Optional.of(podcast));
		mockMvc.perform(get("/api/podcasts/{id}", id)).andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.title").value(podcast.getTitle()))
			.andExpect(jsonPath("$.guid").value(podcast.getGuid()))
			.andExpect(jsonPath("$.description").value(podcast.getDescription()))
			.andExpect(jsonPath("$.author").value(podcast.getAuthor()))
			.andExpect(jsonPath("$.feedURL").value(podcast.getFeedURL()))
			.andExpect(jsonPath("$.rank").value(podcast.getRank()))
			.andExpect(jsonPath("$.lastPubDate").value(formatter.format(podcast.getLastPubDate())))
			.andExpect(jsonPath("$.createdAt").value(formatter.format(podcast.getCreatedAt())))
			.andExpect(jsonPath("$.updatedAt").value(formatter.format(podcast.getUpdatedAt())))
			.andExpect(jsonPath("$.artworkURL").value(podcast.getArtworkURL()))
			.andDo(print());
	}
	*/
	
}