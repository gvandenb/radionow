package com.radionow.stream.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.model.Book;


public interface BookService {

	public Book save(Book book);

	public List<Book> findAll();
	public Page<Book> findAll(Pageable paging);

	public Book findByTitle(String title);

	public void saveAll(List<Book> books);

	public Book findById(Long bid);

	public Page<Book> findAllByLanguage(String string, Pageable pageable);
	
	public Book findByFeedUrl(String feedUrl);

}