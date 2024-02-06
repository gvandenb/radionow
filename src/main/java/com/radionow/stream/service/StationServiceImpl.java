package com.radionow.stream.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.dao.StationRepository;
import com.radionow.stream.model.Station;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    @Override
    public List<Station> getStationByCallsign(String callsign) {
        return stationRepository.findByCallsignContaining(callsign);
    }
}
