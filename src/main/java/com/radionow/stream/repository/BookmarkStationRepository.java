package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.BookmarkStation;
import com.radionow.stream.model.FavoriteStation;

public interface BookmarkStationRepository extends JpaRepository<BookmarkStation, Long> {

	List<BookmarkStation> findByUserId(Long userId);
	BookmarkStation findByUserIdAndStationId(Long userId, Long stationId);
}
