package com.radionow.stream.service;

import java.util.List;

import com.radionow.stream.model.Station;

public interface StationService {

	public List<Station> getStationByCallsign(String callsign);

}
