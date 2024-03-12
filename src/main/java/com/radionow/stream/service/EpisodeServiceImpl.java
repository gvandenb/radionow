package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Episode;
import com.radionow.stream.repository.EpisodeRepository;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    public Episode getEpisodeByGuid(String guid) {
        return episodeRepository.findByGuid(guid);
    }

	public void deleteEpisodeByPodcastId(long podcastId) {
		episodeRepository.deleteEpisodeByPodcastId(podcastId);
	}

	public Episode findByGuid(String guid) {
		// TODO Auto-generated method stub
		return episodeRepository.findByGuid(guid);
	}

	@Override
	public Page<Episode> findByPodcastId(long id, Pageable paging) {
		// TODO Auto-generated method stub
		return episodeRepository.findByPodcastId(id, paging);
	}

	@Override
	public void save(Episode episode) {
		// TODO Auto-generated method stub
		episodeRepository.save(episode);
	}

	@Override
	public List<Episode> findAll() {
		// TODO Auto-generated method stub
		return episodeRepository.findAll();
	}
    
    
}
