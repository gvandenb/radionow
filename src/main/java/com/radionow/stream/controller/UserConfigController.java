package com.radionow.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.UserConfig;
import com.radionow.stream.service.UserConfigService;
import com.radionow.stream.service.UserService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserConfigController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserConfigService userConfigService;
	
	@PostMapping("/users/{uid}/config")
	public ResponseEntity<UserConfig> createUserConfig(@PathVariable("uid") Long id, @RequestBody UserConfig userConfig) {
		try {
			
			UserConfig userConfigExisting = userConfigService.findUserConfigByUserId(id);
			if (userConfigExisting == null) {
				userConfig.setUserId(id);
				userConfigExisting = userConfigService.save(userConfig);
			}
			
			return new ResponseEntity<>(userConfigExisting, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{uid}/config")
	public ResponseEntity<UserConfig> getUserConfig(@PathVariable("uid") Long id) {
		try {
			
			UserConfig userConfig = userConfigService.findUserConfigByUserId(id);
			
			return new ResponseEntity<>(userConfig, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
