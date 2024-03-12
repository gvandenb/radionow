package com.radionow.stream.search.repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.radionow.stream.search.model.SearchStation;

@Repository
public interface StationSearchRepository extends ElasticsearchRepository<SearchStation, Long> {


}
