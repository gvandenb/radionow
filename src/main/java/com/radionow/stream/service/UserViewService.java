package com.radionow.stream.service;

import java.util.List;

import com.radionow.stream.model.UserView;

public interface UserViewService {
	
	UserView findUserViewByUserIdAndObjectId(Long userId, String objectId);
	
	List<UserView> findUserViewByUserId(Long userId);

	UserView save(UserView userView);
}
