package com.radionow.stream.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "statistics")
public class Statistic {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	// clicked on a podcast, station or episode
	@Column(name = "views")
	private Long views;
	
	// clicked on a station
	@Column(name = "statisticType")
	private StatisticType  statisticType;
	
	public Statistic() {}
	
	public Statistic(Long views, StatisticType statisticType) {
		super();
		this.views = views;
		this.statisticType = statisticType;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getViews() {
		return views;
	}


	public void setViews(Long views) {
		this.views = views;
	}


	public StatisticType getStatisticType() {
		return statisticType;
	}


	public void setStatisticType(StatisticType statisticType) {
		this.statisticType = statisticType;
	}
	
	public static enum StatisticType {
		 USER, PODCAST, STATION, EPISODE
		}
	
	
}


