package com.radionow.stream.service;


import java.util.List;

import com.radionow.stream.model.BookmarkStation;

public interface BookmarkStationService {

	BookmarkStation save(BookmarkStation station);
	List<BookmarkStation> findByUserId(long id);
	BookmarkStation findByUserIdAndStationId(Long id, Long sid);
	void delete(BookmarkStation bs);

}
