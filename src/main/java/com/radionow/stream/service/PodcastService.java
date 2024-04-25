package com.radionow.stream.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.model.Podcast;

public interface PodcastService {

	public Podcast getPodcastById(Long id);

	public Podcast save(Podcast podcast);

	public List<Podcast> findAll();

	public List<Podcast> findByTitleContaining(String title);

	public Optional<Podcast> findById(long id);

	public Podcast findByTitle(String title);

	public Podcast findByFeedURL(String feedUrl);

	public List<String> findAllFeedurl();

	public void deletePodcastById(long id);

	public Page<Podcast> findAll(Pageable paging);

	public List<Podcast> findByCategoriesNameOrderByRankAsc(String categoryName, Pageable paging);

	public List<Podcast> findByCategoriesNameOrderByLastPubDateDesc(String categoryName, Pageable paging);

}
