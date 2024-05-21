package com.radionow.stream.service;


import java.util.List;

import com.radionow.stream.model.BookmarkEpisode;


public interface BookmarkEpisodeService {

	BookmarkEpisode save(BookmarkEpisode episode);

	BookmarkEpisode findByUserIdAndEpisodeId(Long id, Long id2);

	void delete(BookmarkEpisode be);

	List<BookmarkEpisode> findByUserId(long id);
}
