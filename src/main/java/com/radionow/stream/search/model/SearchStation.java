package com.radionow.stream.search.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;

import com.radionow.stream.model.Category;
import com.radionow.stream.search.model.SearchStatistic;
import com.radionow.stream.search.model.SearchStatistic.StatisticType;

import jakarta.persistence.*; // for Spring Boot 3


@Document(indexName = "search_stations")
public class SearchStation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "callsign")
	private String callsign;
	
	@Column(name = "frequency")
	private String frequency;
	
	@Column(name = "description")
	private String description;
	
	@Column(length = 2048, name = "url")
	private String url;
	
	@Column(name = "categories")
	@OneToMany(cascade = CascadeType.ALL)
	private List<SearchCategory> categories = new ArrayList<SearchCategory>();
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "stations_search_statistics", 
      joinColumns = 
        { @JoinColumn(name = "stations_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "statistics_id", referencedColumnName = "id") })
	private SearchStatistic statistic = new SearchStatistic(1L, StatisticType.STATION);
	

	public SearchStation() {}
	
	public SearchStation(String title, String callsign, String frequency, String description, String url) {

		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
	}
	
	public SearchStation(String id, String title, String callsign, String frequency, String description, String url) {

		this.id = id;
		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
	}

	public SearchStation(String title, String callsign, String frequency, String description, String url,
			List<SearchCategory> categories, SearchStatistic statistic) {
		super();
		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
		this.categories = categories;
		this.statistic = statistic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<SearchCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<SearchCategory> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", title=" + title + ", callsign=" + callsign + ", frequency=" + frequency
				+ ", description=" + description + ", url=" + url + "]";
	}	

	public SearchStatistic getStatistic() {
		return statistic;
	}

	public void setStatistic(SearchStatistic statistic) {
		this.statistic = statistic;
	}
	
	

}
