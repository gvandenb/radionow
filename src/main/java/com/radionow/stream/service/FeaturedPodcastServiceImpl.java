package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.FeaturedPodcast;
import com.radionow.stream.repository.FeaturedPodcastRepository;

@Service
public class FeaturedPodcastServiceImpl implements FeaturedPodcastService {

	@Autowired
    private FeaturedPodcastRepository featuredPodcastRepository;
	
	@Override
	public List<FeaturedPodcast> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime) {
		// TODO Auto-generated method stub
		List<FeaturedPodcast> featuredPodcastList = featuredPodcastRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(currentTime, currentTime);

		return featuredPodcastList;
	}

	@Override
	public FeaturedPodcast save(FeaturedPodcast podcast) {
		// TODO Auto-generated method stub

		return featuredPodcastRepository.save(podcast);
	}

}
