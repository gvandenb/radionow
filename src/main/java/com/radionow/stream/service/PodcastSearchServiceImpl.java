package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.radionow.stream.search.model.SearchPodcast;
import com.radionow.stream.search.repository.PodcastSearchRepository;

	
@Service
public class PodcastSearchServiceImpl implements PodcastSearchService {

    @Autowired
    private PodcastSearchRepository podcastSearchRepository;

    public SearchPodcast getPodcastById(Long id) {
        return podcastSearchRepository.getPodcastById(id);
    }

	@Override
	public SearchPodcast save(SearchPodcast podcast) {
		// TODO Auto-generated method stub
		return podcastSearchRepository.save(podcast);
	}

	@Override
	public Page<SearchPodcast> findAllByDescriptionContaining(String query, Pageable page) {
		// TODO Auto-generated method stub
		return podcastSearchRepository.findAllByDescriptionContaining(query, page);
	}

	@Override
	public void saveAll(List<SearchPodcast> searchPodcasts) {
		// TODO Auto-generated method stub
		podcastSearchRepository.saveAll(searchPodcasts);
	}


}
