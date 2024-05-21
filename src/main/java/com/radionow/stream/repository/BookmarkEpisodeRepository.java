package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.BookmarkEpisode;
import com.radionow.stream.model.FavoriteEpisode;


public interface BookmarkEpisodeRepository extends JpaRepository<BookmarkEpisode, Long> {

	List<BookmarkEpisode> findByUserId(Long userId);

	BookmarkEpisode findByUserIdAndEpisodeId(Long id, Long pid);
}
