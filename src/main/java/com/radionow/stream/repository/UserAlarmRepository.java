package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.radionow.stream.model.UserAlarm;

public interface UserAlarmRepository extends JpaRepository<UserAlarm, Long> {

	@Query( value = "SELECT ua.* FROM user_alarms ua "
			+ "WHERE ua.user_id = :userId "
			+ "ORDER BY to_char(ua.alarm_time,'HH24:MI:SS') ASC ",
			nativeQuery=true)	
	List<UserAlarm> findUserAlarmByUserId(Long userId);

	UserAlarm getUserAlarmById(Long id);
	
	@Query( value = "SELECT ua.* FROM user_alarms ua "
			+ "WHERE ua.user_id = :userId "
			+ "AND ua.enabled = true "
			+ "AND ua.repeat_days LIKE CONCAT('%', :dayOfWeek, '%') "
			//+ "AND to_char(alarm_time,'HH24:MI:SS') > to_char(CURRENT_TIMESTAMP,'HH24:MI:SS') "
			+ "ORDER BY to_char(ua.alarm_time,'HH24:MI:SS') ASC ",
			//+ "LIMIT 1",
			nativeQuery=true)
	List<UserAlarm> getUserAlarmByUserIdAndDayOfWeek(Long userId, String dayOfWeek);
}
