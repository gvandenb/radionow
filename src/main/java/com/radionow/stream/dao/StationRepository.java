package com.radionow.stream.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

	List<Station> findByTitleContaining(String title);
	
	List<Station> findByCallsignContaining(String callsign);
	
	List<Station> findByFrequencyContaining(String frequency);
	

}
