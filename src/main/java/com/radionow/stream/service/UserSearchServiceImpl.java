package com.radionow.stream.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radionow.stream.search.model.SearchUser;
import com.radionow.stream.search.repository.UserSearchRepository;

@Service
public class UserSearchServiceImpl implements UserSearchService {

    @Autowired
    private UserSearchRepository userSearchRepository;

    @Override
    public SearchUser getUserById(Long id) {
        return userSearchRepository.getUserById(id);
    }

}
