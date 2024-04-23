package com.radionow.stream.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Book;
import com.radionow.stream.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
    private BookRepository bookRepository;
	
	@Override
	public Book save(Book book) {
		// TODO Auto-generated method stub
		return bookRepository.save(book);
	}

	@Override
	public List<Book> findAll() {
		// TODO Auto-generated method stub
		return bookRepository.findAll();
	}

	@Override
	public Page<Book> findAll(Pageable paging) {
		// TODO Auto-generated method stub
		return bookRepository.findAll(paging);
	}

	@Override
	public Book findByTitle(String title) {
		// TODO Auto-generated method stub
		return bookRepository.findByTitle(title);
	}

	@Override
	public void saveAll(List<Book> books) {
		// TODO Auto-generated method stub
		bookRepository.saveAll(books);
	}

	@Override
	public Book findById(Long bid) {
		// TODO Auto-generated method stub
		return bookRepository.findById(bid).get();
	}

	@Override
	public Page<Book> findAllByLanguage(String language, Pageable pageable) {
		// TODO Auto-generated method stub
		return bookRepository.findAllByLanguage(language, pageable);
	}

	@Override
	public Book findByFeedUrl(String feedUrl) {
		// TODO Auto-generated method stub
		return bookRepository.findByFeedUrl(feedUrl);	
	}

}
