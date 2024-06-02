package com.radionow.stream.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.UserAlarm;
import com.radionow.stream.model.UserConfig;
import com.radionow.stream.repository.UserAlarmRepository;
import com.radionow.stream.repository.UserConfigRepository;

@Service
public class UserAlarmServiceImpl implements UserAlarmService {

	@Autowired
    private UserAlarmRepository userAlarmRepository;

	@Override
	public List<UserAlarm> findUserAlarmByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userAlarmRepository.findUserAlarmByUserId(userId);
	}

	@Override
	public UserAlarm save(UserAlarm userAlarm) {
		// TODO Auto-generated method stub
		return userAlarmRepository.save(userAlarm);
	}

	@Override
	public UserAlarm getUserAlarmById(Long id) {
		// TODO Auto-generated method stub
		return userAlarmRepository.getUserAlarmById(id);
	}

	@Override
	public void delete(UserAlarm ua) {
		// TODO Auto-generated method stub
		userAlarmRepository.delete(ua);
	}

	@Override
	public List<UserAlarm> getUserAlarmByUserIdAndDayOfWeek(Long userId, String dayOfWeek) {
		// TODO Auto-generated method stub
		return userAlarmRepository.getUserAlarmByUserIdAndDayOfWeek(userId, dayOfWeek);
	}
	

	
	
}
