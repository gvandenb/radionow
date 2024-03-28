package com.radionow.stream.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.UserView;
import com.radionow.stream.repository.UserViewRepository;

@Service
public class UserViewServiceImpl implements UserViewService {

	@Autowired
    private UserViewRepository userViewRepository;
	
	@Override
	public UserView findUserViewByUserIdAndObjectId(Long userId, String objectId) {
		// TODO Auto-generated method stub
		return userViewRepository.findUserViewByUserIdAndObjectId(userId, objectId);
	}

	@Override
	public UserView save(UserView userView) {
		// TODO Auto-generated method stub
		return userViewRepository.save(userView);
	}

	@Override
	public List<UserView> findUserViewByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userViewRepository.findUserViewByUserId(userId);
	}
	
}
