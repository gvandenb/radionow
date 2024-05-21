package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.BookmarkPodcast;

import com.radionow.stream.repository.BookmarkPodcastRepository;

@Service
public class BookmarkPodcastServiceImpl implements BookmarkPodcastService {

	@Autowired
    private BookmarkPodcastRepository bookmarkPodcastRepository;


	@Override
	public BookmarkPodcast save(BookmarkPodcast podcast) {
		// TODO Auto-generated method stub

		return bookmarkPodcastRepository.save(podcast);
	}


	@Override
	public BookmarkPodcast findByUserIdAndPodcastId(Long id, Long pid) {
		// TODO Auto-generated method stub
		return bookmarkPodcastRepository.findByUserIdAndPodcastId(id, pid);
	}


	@Override
	public void delete(BookmarkPodcast bp) {
		// TODO Auto-generated method stub
		bookmarkPodcastRepository.delete(bp);
	}


	@Override
	public List<BookmarkPodcast> findByUserId(long id) {
		// TODO Auto-generated method stub
		return bookmarkPodcastRepository.findByUserId(id);
	}

}
