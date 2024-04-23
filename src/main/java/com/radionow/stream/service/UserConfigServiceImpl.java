package com.radionow.stream.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.UserConfig;
import com.radionow.stream.repository.UserConfigRepository;

@Service
public class UserConfigServiceImpl implements UserConfigService {

	@Autowired
    private UserConfigRepository userConfigRepository;

	@Override
	public UserConfig findUserConfigByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userConfigRepository.findUserConfigByUserId(userId);
	}

	@Override
	public UserConfig save(UserConfig userConfig) {
		// TODO Auto-generated method stub
		return userConfigRepository.save(userConfig);
	}
	

	
	
}
