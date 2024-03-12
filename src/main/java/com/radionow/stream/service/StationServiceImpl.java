package com.radionow.stream.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Station;
import com.radionow.stream.repository.StationRepository;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    public List<Station> findAll() {
    	return stationRepository.findAll();
    }
    
    public List<Station> getStationByCallsign(String callsign) {
        return stationRepository.findByCallsignContaining(callsign);
    }

	@Override
	public Iterable<Station> findByTitleContaining(String title) {
		// TODO Auto-generated method stub
		return stationRepository.findByTitleContaining(title);
	}

	@Override
	public Optional<Station> findById(long id) {
		// TODO Auto-generated method stub
		return stationRepository.findById(id);
	}

	@Override
	public Station save(Station station) {
		// TODO Auto-generated method stub
		return stationRepository.save(station);
	}

	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		stationRepository.deleteById(id);
	}

	@Override
	public List<Station> findByCallsignContaining(String upperCase) {
		// TODO Auto-generated method stub
		return stationRepository.findByCallsignContaining(upperCase);
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		stationRepository.deleteAll();
	}
}
