package com.radionow.stream.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FavoriteEpisode;


public interface FavoriteEpisodeRepository extends JpaRepository<FavoriteEpisode, Long> {

	List<FavoriteEpisode> findByUserId(Long userId);

	FavoriteEpisode findByUserIdAndEpisodeId(Long id, Long pid);
}
