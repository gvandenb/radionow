package com.radionow.stream.service;


import java.util.List;
import java.util.Optional;


import com.radionow.stream.model.Podcast;

public interface PodcastService {

	public Podcast getPodcastById(Long id);

	public Podcast save(Podcast podcast);

	public List<Podcast> findAll();

	public List<Podcast> findByTitleContaining(String title);

	public Optional<Podcast> findById(long id);

	public Podcast findByTitle(String title);

}
