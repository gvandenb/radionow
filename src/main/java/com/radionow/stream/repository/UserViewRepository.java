package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.UserView;

public interface UserViewRepository extends JpaRepository<UserView, Long> {

	UserView findUserViewByUserIdAndObjectId(Long userId, String objectId);
	List<UserView> findUserViewByUserId(Long userId);

}
