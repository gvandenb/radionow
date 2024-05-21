package com.radionow.stream.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.BookmarkPodcast;
import com.radionow.stream.model.UserAlarm;
import com.radionow.stream.model.UserConfig;
import com.radionow.stream.service.UserAlarmService;
import com.radionow.stream.service.UserConfigService;
import com.radionow.stream.service.UserService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserAlarmController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserAlarmService userAlarmService;
	
	@PostMapping("/users/{uid}/alarms")
	public ResponseEntity<UserAlarm> createUserAlarm(@PathVariable("uid") Long id, @RequestBody UserAlarm userAlarm) {
		try {
			
			UserAlarm savedUserAlarm = userAlarmService.save(userAlarm);
			
			return new ResponseEntity<>(savedUserAlarm, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{uid}/alarms")
	public ResponseEntity<List<UserAlarm>> getUserAlarms(@PathVariable("uid") Long id) {
		try {
			
			List<UserAlarm> userAlarmList = userAlarmService.findUserAlarmByUserId(id);
			
			return new ResponseEntity<>(userAlarmList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/{id}/alarms/{aid}")
	public ResponseEntity<Long> deleteUserAlarmById(@PathVariable("id") Long id, @PathVariable("aid") Long aid) {
		try {
			 
			UserAlarm ua = userAlarmService.getUserAlarmById(aid);
			if (ua == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			userAlarmService.delete(ua);
			return new ResponseEntity<>(ua.getId(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{uid}/alarms/next")
	public ResponseEntity<UserAlarm> getNextUserAlarm(@PathVariable("uid") Long id) {

		for (int i=0; i<7; i++) {
			DayOfWeek day = LocalDate.now().getDayOfWeek().plus(i);
			System.out.println("Day of Week: " + StringUtils.capitalize(day.name().toLowerCase()));
			String capitalizedDayOfWeek = StringUtils.capitalize(day.name().toLowerCase());
			UserAlarm userAlarm = userAlarmService.getUserAlarmByUserIdAndDayOfWeek(id, capitalizedDayOfWeek);
			if (userAlarm != null) {
				System.out.println("Found 1 UserAlarm");
				return new ResponseEntity<>(userAlarm, HttpStatus.OK);
			}
			else {
				System.out.println("Found 0 UserAlarm for " + capitalizedDayOfWeek);

			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

}
