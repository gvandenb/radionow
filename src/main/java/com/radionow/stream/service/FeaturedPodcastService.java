package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import com.radionow.stream.model.FeaturedPodcast;


public interface FeaturedPodcastService {

	List<FeaturedPodcast> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime);
	FeaturedPodcast save(FeaturedPodcast podcast);
}
