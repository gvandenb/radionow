package com.radionow.stream.service;


import java.util.List;
import java.util.Optional;

import com.radionow.stream.model.User;

public interface UserService {

	public User getUserById(Long id);

	public User save(User user);

	public List<User> findAll();

	public List<User> findByEmailContaining(String email);

	public Optional<User> findById(Long id);


}
