package com.radionow.stream.search.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.radionow.stream.search.model.SearchStation;
import com.radionow.stream.search.repository.StationSearchRepository;
import com.radionow.stream.util.ESUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import org.elasticsearch.index.query.Operator;

@Service
public class StationSearchServiceImpl implements StationSearchService {

	@Autowired
    private StationSearchRepository stationSearchRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    
	public SearchStation save(SearchStation station) {
		// TODO Auto-generated method stub
		return stationSearchRepository.save(station);
	}

	@Override
	public void saveAll(List<SearchStation> searchStations) {
		// TODO Auto-generated method stub
		stationSearchRepository.saveAll(searchStations);
	}

	@Override
	public SearchResponse<SearchStation> multiMatch(String key , List<String> fields, Integer from, Integer size) throws IOException {
		
		Map<String, Float> fieldMap = fields.stream() 
				.collect(Collectors.toMap(Function.identity(), a -> 1f));
		/*
		SearchRequest searchRequest = new SearchRequest("search_stations");
		String queryString = key;
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		MultiMatchQueryBuilder multiMatchQueryBuilder1 = new MultiMatchQueryBuilder(queryString);
			
		multiMatchQueryBuilder1.fields(fieldMap).operator(Operator.AND);  //.operator(Operator.And);
		searchSourceBuilder.query(multiMatchQueryBuilder1).from(from).size(size);
		searchRequest.source(searchSourceBuilder);

		SearchResponse<SearchStation> searchResponse = elasticsearchClient.search(searchRequest);
		*/
		
        Supplier<Query> supplier  = ESUtil.supplierQueryForMultiMatchQuery(key,fields);
        SearchResponse<SearchStation> searchResponse =
                elasticsearchClient.search(s->s.index("search_stations").query(supplier.get())
                		.from(from)
                		.size(size)
                	, SearchStation.class);
        System.out.println("es query" +supplier.get().toString());
        return searchResponse;
        
        
    }
	
}
