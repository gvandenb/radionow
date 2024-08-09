package com.radionow.stream.model;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_alarms")
public class UserAlarm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Builder.Default
	@Column(name = "guid")
	private String guid = UUID.randomUUID().toString();
	
	@Column(name = "user_id")
    private Long userId;
	
	@Column(name = "station_id")
    private Long stationId;
	
	@Column(name = "enabled")
    private Boolean enabled;
	
	@Column(name = "enable_snooze")
    private Boolean enableSnooze;
	
	@Column(name = "snooze_interval")
    private Integer snoozeInterval;
	
	// SMTWRFS
	@Column(name = "repeat_days")
    private String repeatDays;
	
	@Column(length = 2048, name = "image_url")
    private String imageUrl;
	
	@Column(length = 2048, name = "play_url")
    private String playUrl;
	
	@Column(length = 2048, name = "title")
    private String title;
	
	@Column(length = 2048, name = "description")
    private String description;
	
	@Column(name = "volume_level")
    private Double volumeLevel;
	
	@Column(name = "alarm_time")
	private Date alarmTime;
	
	@Column(name = "alarm_hour")
    private Integer alarmHour;
	
	@Column(name = "alarm_minute")
    private Integer alarmMinute;
	
	@Column(name = "alarm_meridian")
    private String alarmMeridian;
	
	@Column(name = "created_at")
	@CreationTimestamp
    private Date createdAt;
    
	@Column(name = "updated_at")
	@UpdateTimestamp
    private Date updatedAt;
}
