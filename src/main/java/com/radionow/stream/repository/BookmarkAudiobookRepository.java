package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.BookmarkAudiobook;
import com.radionow.stream.model.FavoriteBook;


public interface BookmarkAudiobookRepository extends JpaRepository<BookmarkAudiobook, Long> {

	List<BookmarkAudiobook> findByUserId(Long userId);

	BookmarkAudiobook findByUserIdAndBookId(Long id, Long pid);
}