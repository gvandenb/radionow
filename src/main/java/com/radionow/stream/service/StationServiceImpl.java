package com.radionow.stream.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Station;
import com.radionow.stream.repository.StationRepository;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    @Override
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

	@Override
	public Object saveAll(List<Station> stations) {
		// TODO Auto-generated method stub
		return stationRepository.saveAll(stations);
	}

	@Override
	public Page<Station> findAll(Pageable paging) {
		// TODO Auto-generated method stub
    	return stationRepository.findAll(paging);
	}

	@Override
	public Station findByGuid(String guid) {
		// TODO Auto-generated method stub
		return stationRepository.findByGuid(guid);
	}

	@Override
	public List<Station> findByCategoriesName(String categoryName, Pageable paging) {
		// TODO Auto-generated method stub
		return stationRepository.findByCategoriesName(categoryName, paging);
	}

	@Override
	public Page<Station> findAllByPublished(Pageable paging, Boolean published) {
		// TODO Auto-generated method stub
		return stationRepository.findAllByPublished(paging, published);
	}

	@Override
	public List<Station> findByCategoriesNameAndPublished(String name, Pageable paging, Boolean isPublished) {
		// TODO Auto-generated method stub
		return stationRepository.findByCategoriesNameAndPublished(name, paging, isPublished);
	}
	
	@Override
	public List<Station> findByCategoriesNameAndCountryAndPublished(String name, String country, Boolean isPublished, Pageable paging) {
		// TODO Auto-generated method stub
		System.out.println("Offset: " + paging.getOffset());
		System.out.println("Page Size: " + paging.getPageSize());
		return stationRepository.findByCategoriesNameAndCountryAndPublished(name, country, isPublished, paging.getPageSize(), paging.getOffset());
	}
}
