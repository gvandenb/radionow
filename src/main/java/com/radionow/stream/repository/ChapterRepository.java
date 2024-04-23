package com.radionow.stream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

}
