package com.radionow.stream.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.FavoriteStation;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, Long> {

	List<FavoriteStation> findByUserId(Long userId);
	FavoriteStation findByUserIdAndStationId(Long userId, Long stationId);
}
