package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import com.radionow.stream.model.FeaturedStation;

public interface FeaturedStationService {

	List<FeaturedStation> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime);
	FeaturedStation save(FeaturedStation station);

}
