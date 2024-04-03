package com.radionow.stream.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;
import org.springframework.data.elasticsearch.annotations.Document;

import com.radionow.stream.model.Statistic.StatisticType;

import jakarta.persistence.*; // for Spring Boot 3

@Entity
@Table(name = "stations")
public class Station {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 2048, name = "title")
	private String title;
	
	@Column(name = "callsign")
	private String callsign;
	
	@Column(name = "frequency")
	private String frequency;
	
	@Column(length = 4096, name = "description")
	private String description;
	
	@NaturalId
	@Column(name = "guid")
	private String guid = UUID.randomUUID().toString();
	
	@Column(length = 4096, name = "url")
	private String url;
	
	@Column(name = "categories")
	@OneToMany(cascade = CascadeType.ALL)
	private List<Category> categories = new ArrayList<Category>();
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "stations_statistics", 
      joinColumns = 
        { @JoinColumn(name = "stations_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "statistics_id", referencedColumnName = "id") })
	private Statistic statistic;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	
	@Column(length = 4096, name = "imageUrl")
	private String imageUrl;
	
	@Column(name = "language")
	private String language;

	public Station() {}
	
	public Station(String title, String callsign, String frequency, String description, String url) {

		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
	}
	
	public Station(Long id, String title, String callsign, String frequency, String description, String url) {

		this.id = id;
		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
	}

	public Station(String title, String callsign, String frequency, String description, String url,
			List<Category> categories, Statistic statistic) {
		super();
		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
		this.categories = categories;
		this.statistic = statistic;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}	

	public Statistic getStatistic() {
		return statistic;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", title=" + title + ", callsign=" + callsign + ", frequency=" + frequency
				+ ", description=" + description + ", guid=" + guid + ", url=" + url + ", categories=" + categories
				+ ", statistic=" + statistic + ", city=" + city + ", state=" + state + ", country=" + country
				+ ", imageUrl=" + imageUrl + ", language=" + language + "]";
	}

	
	
	

}
