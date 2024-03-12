package com.radionow.stream.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.search.model.SearchPodcast;

public interface PodcastSearchService {

	public SearchPodcast getPodcastById(Long id);

	public SearchPodcast save(SearchPodcast podcast);

	public Page<SearchPodcast> findAllByDescriptionContaining(String query, Pageable paging);

	public void saveAll(List<SearchPodcast> searchPodcasts);
}
