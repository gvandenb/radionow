package com.radionow.stream.service;

import java.util.List;
import java.util.Optional;

import com.radionow.stream.model.Station;

public interface StationService {

	public List<Station> getStationByCallsign(String callsign);

	public Iterable<Station> findAll();

	public Iterable<Station> findByTitleContaining(String title);

	public Optional<Station> findById(long id);

	public Station save(Station station);

	public void deleteById(long id);

	public List<Station> findByCallsignContaining(String upperCase);

	public void deleteAll();
	
	

}
