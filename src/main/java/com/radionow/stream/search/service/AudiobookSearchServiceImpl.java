package com.radionow.stream.search.service;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.search.model.SearchAudiobook;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.repository.AudiobookSearchRepository;
import com.radionow.stream.search.repository.EpisodeSearchRepository;
import com.radionow.stream.util.ESUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;

@Service
public class AudiobookSearchServiceImpl implements AudiobookSearchService {

	@Autowired
    private AudiobookSearchRepository audiobookSearchRepository;
	
	@Autowired
    private ElasticsearchClient elasticsearchClient;
	
	@Override
	public void saveAll(List<SearchAudiobook> searchAudiobooks) {
		// TODO Auto-generated method stub
		audiobookSearchRepository.saveAll(searchAudiobooks);
	}

	@Override
	public SearchResponse<SearchAudiobook> multiMatch(String key, List<String> fields, Integer from, Integer size)
			throws IOException {
		// TODO Auto-generated method stub
		Supplier<Query> supplier  = ESUtil.supplierQueryForMultiMatchQuery(key,fields);
        SearchResponse<SearchAudiobook> searchResponse =
                elasticsearchClient.search(s->s.index("search_audiobooks")
                		.query(supplier.get())
                		.from(from)
                		.size(size).trackTotalHits(tth -> tth.enabled(true))
            		, SearchAudiobook.class);
        
        System.out.println("es query" +supplier.get().toString());
        return searchResponse;	
        
	}

}
