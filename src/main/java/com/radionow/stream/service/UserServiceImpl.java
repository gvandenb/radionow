package com.radionow.stream.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.model.User;
import com.radionow.stream.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		// uniquify searchterm list and maintain order
		//List<String> searchtermList = ;
		List<String> newSearchtermList = user.getSearchTerms().stream().distinct().collect(Collectors.toList());
		user.setSearchTerms(newSearchtermList);

		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public List<User> findByEmailContaining(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmailContaining(email);
	}

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

}
