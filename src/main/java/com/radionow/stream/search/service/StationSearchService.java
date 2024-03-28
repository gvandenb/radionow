package com.radionow.stream.search.service;


import java.io.IOException;
import java.util.List;

import com.radionow.stream.search.model.SearchStation;

import co.elastic.clients.elasticsearch.core.SearchResponse;



public interface StationSearchService {

	public SearchStation save(SearchStation station);

	public void saveAll(List<SearchStation> searchStations);

	SearchResponse<SearchStation> multiMatch(String key, List<String> fields, Integer from, Integer size)
			throws IOException;

}
