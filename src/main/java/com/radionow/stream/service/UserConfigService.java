package com.radionow.stream.service;


import com.radionow.stream.model.UserConfig;

public interface UserConfigService {
	
	UserConfig findUserConfigByUserId(Long userId);
	
	UserConfig save(UserConfig userConfig);
}
