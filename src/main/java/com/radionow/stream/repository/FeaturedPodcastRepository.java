package com.radionow.stream.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FeaturedPodcast;


public interface FeaturedPodcastRepository extends JpaRepository<FeaturedPodcast, Long> {
	List<FeaturedPodcast> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime1, Date currentTime2);

}
