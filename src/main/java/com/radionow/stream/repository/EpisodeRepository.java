package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.Episode;

import jakarta.transaction.Transactional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	List<Episode> findByTitleContaining(String title);

    Page<Episode> findByPodcastId(Long podcastId, Pageable pageable);
    
    Episode findByGuid(String guid);
    
    @Transactional
    void deleteEpisodeByPodcastId(long podcastId);

}
