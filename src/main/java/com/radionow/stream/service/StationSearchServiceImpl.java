package com.radionow.stream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.search.model.SearchStation;
import com.radionow.stream.search.repository.StationSearchRepository;

@Service
public class StationSearchServiceImpl implements StationSearchService {

	@Autowired
    private StationSearchRepository stationSearchRepository;
	
	public SearchStation save(SearchStation station) {
		// TODO Auto-generated method stub
		return stationSearchRepository.save(station);
	}

}
