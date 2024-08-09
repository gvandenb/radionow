package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.User;
import com.radionow.stream.model.UserView;
import com.radionow.stream.model.ViewType;

public interface UserRepository extends JpaRepository<User, Long> {

	User getUserById(Long id);
	List<User> findByEmailContaining(String email);
	User findByDeviceId(String id);


}
