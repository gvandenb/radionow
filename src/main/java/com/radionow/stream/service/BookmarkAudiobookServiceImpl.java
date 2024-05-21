package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.BookmarkAudiobook;
import com.radionow.stream.repository.BookmarkAudiobookRepository;

@Service
public class BookmarkAudiobookServiceImpl implements BookmarkAudiobookService {

	@Autowired
    private BookmarkAudiobookRepository bookmarkAudiobookRepository;
	

	@Override
	public BookmarkAudiobook save(BookmarkAudiobook book) {
		// TODO Auto-generated method stub

		return bookmarkAudiobookRepository.save(book);
	}


	@Override
	public BookmarkAudiobook findByUserIdAndBookId(Long id, Long bid) {
		// TODO Auto-generated method stub
		return bookmarkAudiobookRepository.findByUserIdAndBookId(id, bid);
	}


	@Override
	public List<BookmarkAudiobook> findByUserId(long id) {
		// TODO Auto-generated method stub
		return bookmarkAudiobookRepository.findByUserId(id);
	}


	@Override
	public void delete(BookmarkAudiobook bb) {
		// TODO Auto-generated method stub
		bookmarkAudiobookRepository.delete(bb);
	}

}
