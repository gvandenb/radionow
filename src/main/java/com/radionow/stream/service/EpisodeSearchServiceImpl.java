package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Episode;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.repository.EpisodeSearchRepository;

	
@Service
public class EpisodeSearchServiceImpl implements EpisodeSearchService {

    @Autowired
    private EpisodeSearchRepository episodeSearchRepository;

	@Override
	public SearchEpisode getEpisodeById(String id) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.getEpisodeById(id);
	}

	@Override
	public SearchEpisode save(SearchEpisode episode) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.save(episode);
	}

	@Override
	public Page<SearchEpisode> findAllByDescriptionContaining(String query, Pageable paging) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.findAllByDescriptionContaining(query, paging);
	}

	public void createProductIndexBulk(final List<SearchEpisode> episodes) {
		episodeSearchRepository.saveAll(episodes);
	  }

	@Override
	public SearchEpisode getEpisodeByGuid(String guid) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.getEpisodeByGuid(guid);
	}

	@Override
	public void saveAll(List<SearchEpisode> searchEpisodes) {
		// TODO Auto-generated method stub
		episodeSearchRepository.saveAll(searchEpisodes);
	}

}
