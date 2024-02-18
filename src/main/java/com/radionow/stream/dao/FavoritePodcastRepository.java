package com.radionow.stream.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FavoritePodcast;


public interface FavoritePodcastRepository extends JpaRepository<FavoritePodcast, Long> {

	List<FavoritePodcast> findByUserId(Long userId);

	FavoritePodcast findByUserIdAndPodcastId(Long id, Long pid);
}
