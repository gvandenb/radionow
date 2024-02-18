package com.radionow.stream.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Podcast;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {

	List<Podcast> findByTitleContaining(String title);

}
