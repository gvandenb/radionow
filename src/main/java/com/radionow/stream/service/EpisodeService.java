package com.radionow.stream.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radionow.stream.model.Episode;

public interface EpisodeService {

	public Episode getEpisodeByGuid(String guid);
	
    void deleteEpisodeByPodcastId(long podcastId);

	public Episode findByGuid(String guid);

	public Page<Episode> findByPodcastId(long id, Pageable paging);

	public void save(Episode episode);

	public List<Episode> findAll();
	public Page<Episode> findAll(Pageable paging);

}
