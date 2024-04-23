package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

	List<Station> findByTitleContaining(String title);
	
	List<Station> findByCallsignContaining(String callsign);
	
	List<Station> findByFrequencyContaining(String frequency);
	
	Station findByGuid(String guid);
	
	List<Station> findByCategoriesName(String categoryName, Pageable paging);

	Page<Station> findAllByPublished(Pageable paging, Boolean published);

	List<Station> findByCategoriesNameAndPublished(String name, Pageable paging, Boolean isPublished);
}
