package com.radionow.stream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User getUserById(Long id);
	List<User> findByEmailContaining(String email);

}
