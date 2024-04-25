package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import com.radionow.stream.model.FeaturedAudiobook;

public interface FeaturedAudiobookService {

	List<FeaturedAudiobook> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime);
	FeaturedAudiobook save(FeaturedAudiobook book);

}
