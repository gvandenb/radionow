package com.radionow.stream.service;


import java.util.List;

import com.radionow.stream.model.UserAlarm;

public interface UserAlarmService {
	
	List<UserAlarm> findUserAlarmByUserId(Long userId);
	
	UserAlarm save(UserAlarm userAlarm);

	UserAlarm getUserAlarmById(Long id);

	void delete(UserAlarm ua);
	
	UserAlarm getUserAlarmByUserIdAndDayOfWeek(Long userId, String dayOfWeek);
}
