package com.radionow.stream.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.FeaturedStation;
import com.radionow.stream.repository.FeaturedStationRepository;

@Service
public class FeaturedStationServiceImpl implements FeaturedStationService {

	@Autowired
    private FeaturedStationRepository featuredStationRepository;
	
	@Override
	public List<FeaturedStation> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime) {
		// TODO Auto-generated method stub
		List<FeaturedStation> featuredStationList = featuredStationRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(currentTime, currentTime);

		return featuredStationList;
	}

	@Override
	public FeaturedStation save(FeaturedStation station) {
		// TODO Auto-generated method stub

		return featuredStationRepository.save(station);
	}

}
