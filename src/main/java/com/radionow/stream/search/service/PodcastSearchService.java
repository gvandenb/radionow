package com.radionow.stream.search.service;


import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.search.model.SearchPodcast;

import co.elastic.clients.elasticsearch.core.SearchResponse;

public interface PodcastSearchService {

	public SearchPodcast getPodcastById(Long id);

	public SearchPodcast save(SearchPodcast podcast);

	public Page<SearchPodcast> findAllByDescriptionContaining(String query, Pageable paging);

	public void saveAll(List<SearchPodcast> searchPodcasts);

	SearchResponse<SearchPodcast> multiMatch(String key, List<String> fields, Integer from, Integer size)
			throws IOException;
}
