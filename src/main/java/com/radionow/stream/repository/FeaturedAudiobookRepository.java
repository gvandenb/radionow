package com.radionow.stream.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FeaturedAudiobook;
import com.radionow.stream.model.FeaturedPodcast;


public interface FeaturedAudiobookRepository extends JpaRepository<FeaturedAudiobook, Long> {
	List<FeaturedAudiobook> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime1, Date currentTime2);

}
