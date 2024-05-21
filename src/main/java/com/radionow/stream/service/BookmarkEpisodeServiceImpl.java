package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.BookmarkEpisode;
import com.radionow.stream.model.BookmarkPodcast;
import com.radionow.stream.model.BookmarkStation;
import com.radionow.stream.model.Station;
import com.radionow.stream.repository.BookmarkEpisodeRepository;
import com.radionow.stream.repository.BookmarkPodcastRepository;

@Service
public class BookmarkEpisodeServiceImpl implements BookmarkEpisodeService {

	@Autowired
    private BookmarkEpisodeRepository bookmarkEpisodeRepository;


	@Override
	public List<BookmarkEpisode> findByUserId(long id) {
		// TODO Auto-generated method stub
		return bookmarkEpisodeRepository.findByUserId(id);
	}


	@Override
	public BookmarkEpisode findByUserIdAndEpisodeId(Long id, Long id2) {
		// TODO Auto-generated method stub
		return bookmarkEpisodeRepository.findByUserIdAndEpisodeId(id, id2);
	}


	@Override
	public void delete(BookmarkEpisode be) {
		// TODO Auto-generated method stub
		bookmarkEpisodeRepository.delete(be);
	}


	@Override
	public BookmarkEpisode save(BookmarkEpisode episode) {
		// TODO Auto-generated method stub
		return bookmarkEpisodeRepository.save(episode);
	}

}
