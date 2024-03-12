package com.radionow.stream.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.model.Episode;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.model.SearchPodcast;

public interface EpisodeSearchService {

	public SearchEpisode save(SearchEpisode episode);

	public Page<SearchEpisode> findAllByDescriptionContaining(String query, Pageable paging);

	SearchEpisode getEpisodeById(String id);
	
	public void createProductIndexBulk(final List<SearchEpisode> episodes);

	public SearchEpisode getEpisodeByGuid(String guid);

	public void saveAll(List<SearchEpisode> searchEpisodes);

}
