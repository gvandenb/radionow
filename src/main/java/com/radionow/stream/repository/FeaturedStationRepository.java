package com.radionow.stream.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FeaturedStation;

public interface FeaturedStationRepository extends JpaRepository<FeaturedStation, Long> {

	List<FeaturedStation> findByStartDateTimeBeforeAndEndDateTimeAfter(Date currentTime1, Date currentTime2);
	

}
