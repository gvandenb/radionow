package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Podcast;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {

	List<Podcast> findByTitleContaining(String title);
	Podcast getPodcastById(Long id);
	Podcast findByTitle(String title);

}
