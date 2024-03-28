package com.radionow.stream.search.service;


import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Episode;
import com.radionow.stream.search.model.SearchEpisode;
import com.radionow.stream.search.repository.EpisodeSearchRepository;
import com.radionow.stream.util.ESUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;

	
@Service
public class EpisodeSearchServiceImpl implements EpisodeSearchService {

    @Autowired
    private EpisodeSearchRepository episodeSearchRepository;
    
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    
	@Override
	public SearchEpisode getEpisodeById(String id) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.getEpisodeById(id);
	}

	@Override
	public SearchEpisode save(SearchEpisode episode) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.save(episode);
	}

	@Override
	public Page<SearchEpisode> findAllByDescriptionContaining(String query, Pageable paging) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.findAllByDescriptionContaining(query, paging);
	}

	public void createProductIndexBulk(final List<SearchEpisode> episodes) {
		episodeSearchRepository.saveAll(episodes);
	  }

	@Override
	public SearchEpisode getEpisodeByGuid(String guid) {
		// TODO Auto-generated method stub
		return episodeSearchRepository.getEpisodeByGuid(guid);
	}

	@Override
	public void saveAll(List<SearchEpisode> searchEpisodes) {
		// TODO Auto-generated method stub
		episodeSearchRepository.saveAll(searchEpisodes);
	}
	
	@Override
	public SearchResponse<SearchEpisode> multiMatch(String key , List<String> fields, Integer from, Integer size) throws IOException {
        Supplier<Query> supplier  = ESUtil.supplierQueryForMultiMatchQuery(key,fields);
        SearchResponse<SearchEpisode> searchResponse =
                elasticsearchClient.search(s->s.index("search_episodes")
                		.query(supplier.get())
                		.from(from)
                		.size(size).trackTotalHits(tth -> tth.enabled(true))
            		, SearchEpisode.class);
        
        System.out.println("es query" +supplier.get().toString());
        return searchResponse;
    }
		/*
	    QueryBuilder queryBuilder = QueryBuilders
	      .wildcardQuery("name", query+"*");
	    NativeQuery searchQuery = new NativeQueryBuilder()
	    		  .withQuery(multiMatchQuery("tutorial")
	    		    .field("title")
	    		    .field("tags")
	    		    .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
	    		  .build();

	    Query searchQuery = new NativeQueryBuilder()
	      .withQuery(multiMatchQuery(query))
	      .withPageable(PageRequest.of(0, 5))
	      .build();

	    SearchHits<Episode> searchSuggestions = 
	      elasticsearchOperations.search(searchQuery, 
	        Product.class,
	      IndexCoordinates.of(PRODUCT_INDEX));
	    
	    List<String> suggestions = new ArrayList<String>();
	    
	    searchSuggestions.getSearchHits().forEach(searchHit->{
	      suggestions.add(searchHit.getContent().getName());
	    });
	    return suggestions;
	    
	    */
	 
}
