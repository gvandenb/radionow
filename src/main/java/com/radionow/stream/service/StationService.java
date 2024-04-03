package com.radionow.stream.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.radionow.stream.model.Station;

public interface StationService {

	public List<Station> getStationByCallsign(String callsign);

	public List<Station> findAll();

	public Iterable<Station> findByTitleContaining(String title);

	public Optional<Station> findById(long id);

	public Station save(Station station);

	public void deleteById(long id);

	public List<Station> findByCallsignContaining(String upperCase);

	public void deleteAll();

	public Object saveAll(List<Station> stations);

	public Page<Station> findAll(Pageable paging);
	
	public Station findByGuid(String guid);

	public List<Station> findByCategoriesName(String categoryName, Pageable paging);

}
