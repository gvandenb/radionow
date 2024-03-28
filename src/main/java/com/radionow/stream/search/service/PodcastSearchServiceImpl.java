package com.radionow.stream.search.service;


import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.radionow.stream.search.model.SearchPodcast;
import com.radionow.stream.search.repository.PodcastSearchRepository;
import com.radionow.stream.util.ESUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;

	
@Service
public class PodcastSearchServiceImpl implements PodcastSearchService {

    @Autowired
    private PodcastSearchRepository podcastSearchRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    
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

	@Override
	public SearchResponse<SearchPodcast> multiMatch(String key , List<String> fields, Integer from, Integer size) throws IOException {
        Supplier<Query> supplier  = ESUtil.supplierQueryForMultiMatchQuery(key,fields);
        SearchResponse<SearchPodcast> searchResponse =
                elasticsearchClient.search(s->s.index("search_podcasts")
                		.query(supplier.get())
                		.from(from)
                		.size(size)
            		, SearchPodcast.class);
        System.out.println("es query" +supplier.get().toString());
        return searchResponse;
    }

}
