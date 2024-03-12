package com.radionow.stream.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.Podcast;
import com.radionow.stream.repository.PodcastRepository;

@Service
public class PodcastServiceImpl implements PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    @Override
    public Podcast getPodcastById(Long id) {
        return podcastRepository.getPodcastById(id);
    }

	@Override
	public Podcast save(Podcast podcast) {
		// TODO Auto-generated method stub
		return podcastRepository.save(podcast);
	}

	@Override
	public List<Podcast> findAll() {
		// TODO Auto-generated method stub
		return podcastRepository.findAll();
	}

	@Override
	public List<Podcast> findByTitleContaining(String title) {
		// TODO Auto-generated method stub
		return podcastRepository.findByTitleContaining(title);
	}

	@Override
	public Optional<Podcast> findById(long id) {
		// TODO Auto-generated method stub
		return podcastRepository.findById(id);
	}

	@Override
	public Podcast findByTitle(String title) {
		// TODO Auto-generated method stub
		return podcastRepository.findByTitle(title);
	}
}
