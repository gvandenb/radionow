package com.radionow.stream.search.repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.radionow.stream.search.model.SearchUser;


@Repository
public interface UserSearchRepository extends ElasticsearchRepository<SearchUser, Long> {

	SearchUser getUserById(Long id);

}
