package com.radionow.stream.service;


import java.util.Collection;
import java.util.List;

import com.radionow.stream.model.BookmarkAudiobook;
import com.radionow.stream.model.Station;

public interface BookmarkAudiobookService {

	BookmarkAudiobook save(BookmarkAudiobook book);

	BookmarkAudiobook findByUserIdAndBookId(Long id, Long bid);

	List<BookmarkAudiobook> findByUserId(long id);

	void delete(BookmarkAudiobook bb);

}
