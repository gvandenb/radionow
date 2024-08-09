package com.radionow.stream.data;

import com.radionow.stream.model.UserAlarm;

import lombok.Data;

@Data
public class AlarmDto {

	private Long id;  // userAlarm.id
	private String day;
	private Integer hour;
	private Integer hourOfDay;
	private Integer minute;
	private Integer dayOfWeek;
	private String timeOfDay;
	private String alarmString;
	private UserAlarm userAlarm;
}
