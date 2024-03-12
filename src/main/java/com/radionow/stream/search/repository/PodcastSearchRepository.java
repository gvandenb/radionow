package com.radionow.stream.search.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.radionow.stream.search.model.SearchPodcast;

@Repository
public interface PodcastSearchRepository extends ElasticsearchRepository<SearchPodcast, Long> {

	SearchPodcast getPodcastById(Long id);
    void deleteById(Long id);
	Page<SearchPodcast> findAllByDescriptionContaining(String query, Pageable page);


}
