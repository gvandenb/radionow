package com.radionow.stream.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.radionow.stream.search.model.SearchAudiobook;

@Repository
public interface AudiobookSearchRepository extends ElasticsearchRepository<SearchAudiobook, Long> {

}
