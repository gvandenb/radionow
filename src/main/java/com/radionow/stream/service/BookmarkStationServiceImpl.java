package com.radionow.stream.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.BookmarkStation;
import com.radionow.stream.repository.BookmarkStationRepository;

@Service
public class BookmarkStationServiceImpl implements BookmarkStationService {

	@Autowired
    private BookmarkStationRepository bookmarkStationRepository;

	@Override
	public BookmarkStation save(BookmarkStation station) {
		// TODO Auto-generated method stub
		return bookmarkStationRepository.save(station);
	}

	@Override
	public List<BookmarkStation> findByUserId(long id) {
		// TODO Auto-generated method stub
		return bookmarkStationRepository.findByUserId(id);
	}

	@Override
	public BookmarkStation findByUserIdAndStationId(Long id, Long sid) {
		// TODO Auto-generated method stub
		return bookmarkStationRepository.findByUserIdAndStationId(id, sid);
	}

	@Override
	public void delete(BookmarkStation bs) {
		// TODO Auto-generated method stub
		bookmarkStationRepository.delete(bs);
	}
	
	

}
