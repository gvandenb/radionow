package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.radionow.stream.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

	List<Station> findByTitleContaining(String title);
	
	List<Station> findByCallsignContaining(String callsign);
	
	List<Station> findByFrequencyContaining(String frequency);
	
	Station findByGuid(String guid);
	
	List<Station> findByCategoriesName(String categoryName, Pageable paging);

	Page<Station> findAllByPublished(Pageable paging, Boolean published);

	List<Station> findByCategoriesNameAndPublished(String name, Pageable paging, Boolean isPublished);

	@Query( value = "SELECT s.*, ss.statistics_id"
					+ " FROM stations s join stations_categories sc on s.id = sc.station_id"
					+ " join categories c on sc.categories_id = c.id "
					+ " join stations_statistics ss on s.id = ss.stations_id"
					+ " join statistic st on ss.statistics_id = st.id"
					+ " WHERE c.name = :name"
					+ " AND s.country = :country"
					+ " AND s.published = :isPublished"
					+ " GROUP BY (s.id,s.title, c.name, st.rb_votes, ss.statistics_id)"
					+ " ORDER BY st.rb_votes Desc Limit 20 \n--#pageable\\n-", 
			countQuery = "SELECT count(s.*), ss.statistics_id"
					+ " FROM stations s join stations_categories sc on s.id = sc.station_id"
					+ " join categories c on sc.categories_id = c.id "
					+ " join stations_statistics ss on s.id = ss.stations_id"
					+ " join statistic st on ss.statistics_id = st.id"
					+ " WHERE c.name = :name"
					+ " AND s.country = :country"
					+ " AND s.published = :isPublished"
					+ " GROUP BY (s.id,s.title, c.name, st.rb_votes, ss.statistics_id)"
					+ " ORDER BY st.rb_votes Desc \n--#pageable\\n-", 			
			
			nativeQuery=true)
	Page<Station> findByCategoriesNameAndCountryAndPublished(String name, String country,
			Boolean isPublished, Pageable paging);
}
