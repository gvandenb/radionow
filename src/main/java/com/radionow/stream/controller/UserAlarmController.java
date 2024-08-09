package com.radionow.stream.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.joda.time.LocalTime;
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

import com.radionow.stream.data.AlarmDto;
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
	public ResponseEntity<AlarmDto> getNextUserAlarm(@PathVariable("uid") Long id) {
		AlarmDto response = null;
		for (int i=0; i<7; i++) {
			DayOfWeek day = LocalDate.now().getDayOfWeek().plus(i);
			System.out.println("Day of Week: " + StringUtils.capitalize(day.name().toLowerCase()));
			String capitalizedDayOfWeek = StringUtils.capitalize(day.name().toLowerCase());
			
			List<UserAlarm> userAlarmList = userAlarmService.getUserAlarmByUserIdAndDayOfWeek(id, capitalizedDayOfWeek);
			System.out.println("Alarms for " + capitalizedDayOfWeek + ": " + userAlarmList.size());
			// Today
			if (i == 0) {
				for (UserAlarm userAlarm : userAlarmList) {
				
					//Calendar alarmTime = Calendar.getInstance();
					//alarmTime.setTime(userAlarm.getAlarmTime());
					int alarmTimeHourOfDay = userAlarm.getAlarmHour();
					int alarmTimeMinute = userAlarm.getAlarmMinute();
					
					Calendar timeNow = Calendar.getInstance();
					int timeNowHourOfDay = timeNow.get(Calendar.HOUR_OF_DAY);
					int timeNowMinute = timeNow.get(Calendar.MINUTE);
					System.out.println("----------------------------------------");

					System.out.println("alarmTimeHourOfDay: " + alarmTimeHourOfDay);
					System.out.println("timeNowHourOfDay: " + timeNowHourOfDay);
					System.out.println("alarmTimeMinute: " + alarmTimeMinute);
					System.out.println("timeNowMinute: " + timeNowMinute);
					if ((alarmTimeHourOfDay > timeNowHourOfDay) || alarmTimeHourOfDay == timeNowHourOfDay && alarmTimeMinute > timeNowMinute) {
						// found next alarm on current day
						response = processUserAlarm(userAlarm, day);
						System.out.println("Found next alarm: " + response.getAlarmString());

						break;
					}
				};
			}
			else {
			// if response is still null, we have no alarms set for the rest of today
			// need to grab first alarm from a subsequent day
				if (userAlarmList.size() > 0) {
					
					response = processUserAlarm(userAlarmList.get(0), day);
					System.out.println("Found next alarm: " + response.getAlarmString());

					return new ResponseEntity<>(response, HttpStatus.OK);

				}
				
			}
			if (response != null) {
				System.out.println("Returning alarm: " + response.getId());
				return new ResponseEntity<>(response, HttpStatus.OK);

			}
			
		}
		
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
	
	private AlarmDto processUserAlarm(UserAlarm userAlarm, DayOfWeek day) {
		AlarmDto alarm = new AlarmDto();
		// 1 for Monday; 7 for Sunday
		//Date alarmTime = userAlarm.getAlarmTime();
		//TimeZone americaPacific = TimeZone.getTimeZone("America/Los_Angeles");
		//Calendar cal = Calendar.getInstance();
		//cal.setTime(alarmTime);
		
		int hour = (userAlarm.getAlarmHour() > 12) ? userAlarm.getAlarmHour() - 12 : userAlarm.getAlarmHour();
		int hourOfDay = userAlarm.getAlarmHour();
		int minute = userAlarm.getAlarmMinute();
		String am_pm = (userAlarm.getAlarmHour() > 11) ? "PM" : "AM";
		String time = ((hour == 0) ? "12":hour) + ":" + ((minute < 10) ? "0":"") + minute + " " + am_pm; 

		alarm.setId(userAlarm.getId());
		//alarm.setUuid(UUID.randomUUID().toString());
		alarm.setDay(day.name().substring(0,3));
		alarm.setHour(hour);
		alarm.setHourOfDay(hourOfDay);
		alarm.setMinute(minute);
		alarm.setTimeOfDay(am_pm);
		alarm.setDayOfWeek(day.getValue());
		alarm.setAlarmString(day.name().substring(0,3) + ", " + time);
		alarm.setUserAlarm(userAlarm);
		
		return alarm;
	}

}
