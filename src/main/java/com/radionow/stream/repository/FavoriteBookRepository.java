package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FavoriteBook;


public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {

	List<FavoriteBook> findByUserId(Long userId);

	FavoriteBook findByUserIdAndBookId(Long id, Long pid);
}