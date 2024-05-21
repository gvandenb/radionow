package com.radionow.stream.service;


import java.util.List;

import com.radionow.stream.model.BookmarkPodcast;


public interface BookmarkPodcastService {

	BookmarkPodcast save(BookmarkPodcast podcast);

	BookmarkPodcast findByUserIdAndPodcastId(Long id, Long pid);

	void delete(BookmarkPodcast bp);

	List<BookmarkPodcast> findByUserId(long id);
}
