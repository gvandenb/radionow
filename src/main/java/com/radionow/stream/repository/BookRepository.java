package com.radionow.stream.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	Book findByTitle(String title);

	Page<Book> findAllByLanguage(String language, Pageable pageable);

	Book findByFeedUrl(String feedUrl);
}
