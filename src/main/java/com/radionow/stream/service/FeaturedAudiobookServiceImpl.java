package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.FeaturedAudiobook;
import com.radionow.stream.model.FeaturedStation;
import com.radionow.stream.repository.FeaturedAudiobookRepository;
import com.radionow.stream.repository.FeaturedStationRepository;

@Service
public class FeaturedAudiobookServiceImpl implements FeaturedAudiobookService {

	@Autowired
    private FeaturedAudiobookRepository featuredAudiobookRepository;
	
	@Override
	public List<FeaturedAudiobook> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime) {
		// TODO Auto-generated method stub
		List<FeaturedAudiobook> featuredAudiobookList = featuredAudiobookRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(currentTime, currentTime);

		return featuredAudiobookList;
	}

	@Override
	public FeaturedAudiobook save(FeaturedAudiobook book) {
		// TODO Auto-generated method stub

		return featuredAudiobookRepository.save(book);
	}

}
