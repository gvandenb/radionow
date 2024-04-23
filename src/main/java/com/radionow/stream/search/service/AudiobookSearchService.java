package com.radionow.stream.search.service;

import java.io.IOException;
import java.util.List;

import com.radionow.stream.search.model.SearchAudiobook;

import co.elastic.clients.elasticsearch.core.SearchResponse;

public interface AudiobookSearchService {

	public void saveAll(List<SearchAudiobook> searchAudiobooks);

	SearchResponse<SearchAudiobook> multiMatch(String key, List<String> fields, Integer from, Integer size) throws IOException;
	
}
