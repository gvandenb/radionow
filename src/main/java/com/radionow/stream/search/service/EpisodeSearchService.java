package com.radionow.stream.search.service;


import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.model.Episode;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchPodcast;

import co.elastic.clients.elasticsearch.core.SearchResponse;

public interface EpisodeSearchService {

	public SearchEpisode save(SearchEpisode episode);

	public Page<SearchEpisode> findAllByDescriptionContaining(String query, Pageable paging);

	SearchEpisode getEpisodeById(String id);
	
	public void createProductIndexBulk(final List<SearchEpisode> episodes);

	public SearchEpisode getEpisodeByGuid(String guid);

	public void saveAll(List<SearchEpisode> searchEpisodes);

	SearchResponse<SearchEpisode> multiMatch(String key, List<String> fields, Integer from, Integer size) throws IOException;


}
