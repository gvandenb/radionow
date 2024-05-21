package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.BookmarkPodcast;
import com.radionow.stream.model.FavoritePodcast;


public interface BookmarkPodcastRepository extends JpaRepository<BookmarkPodcast, Long> {

	List<BookmarkPodcast> findByUserId(Long userId);

	BookmarkPodcast findByUserIdAndPodcastId(Long id, Long pid);
}
