package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import com.radionow.stream.model.FeaturedAudiobook;
import com.radionow.stream.model.FeaturedStation;

public interface FeaturedAudiobookService {

	List<FeaturedAudiobook> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime);
	FeaturedAudiobook save(FeaturedAudiobook book);

}
