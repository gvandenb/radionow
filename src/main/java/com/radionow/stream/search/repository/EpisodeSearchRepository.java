package com.radionow.stream.search.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.radionow.stream.search.model.SearchEpisode;

@Repository
public interface EpisodeSearchRepository extends ElasticsearchRepository<SearchEpisode, Long> {

	SearchEpisode getEpisodeById(String id);

	Page<SearchEpisode> findAllByDescriptionContaining(String query, Pageable paging);

	SearchEpisode getEpisodeByGuid(String guid);

	
    
}
