package com.radionow.stream.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radionow.stream.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmailContaining(String email);

}
