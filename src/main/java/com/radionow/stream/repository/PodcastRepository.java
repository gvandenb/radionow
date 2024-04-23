package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.radionow.stream.model.Podcast;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {

	List<Podcast> findByTitleContaining(String title);
	Podcast getPodcastById(Long id);
	Podcast findByTitle(String title);
	Podcast findByFeedURL(String feedUrl);

	@Query("SELECT p.feedURL FROM Podcast p")              
    public List<String> findAllFeedurl();
	
	List<Podcast> findByCategoriesNameOrderByRankAsc(String categoryName, Pageable paging);

	List<Podcast> findByCategoriesNameOrderByLastPubDateDesc(String categoryName, Pageable paging);
}
