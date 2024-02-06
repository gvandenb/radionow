package com.radionow.stream.model;

import java.util.List;
import jakarta.persistence.*; // for Spring Boot 3

@Entity
@Table(name = "stations")
public class Station {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "callsign")
	private String callsign;
	
	@Column(name = "frequency")
	private String frequency;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "url")
	private String url;
	
	@ManyToMany
	private List<Genre> genres;

	public Station() {}
	
	public Station(String title, String callsign, String frequency, String description, String url,
			List<Genre> genres) {

		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
		this.genres = genres;
	}
	
	public Station(Long id, String title, String callsign, String frequency, String description, String url,
			List<Genre> genres) {

		this.id = id;
		this.title = title;
		this.callsign = callsign;
		this.frequency = frequency;
		this.description = description;
		this.url = url;
		this.genres = genres;
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

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", title=" + title + ", callsign=" + callsign + ", frequency=" + frequency
				+ ", description=" + description + ", url=" + url + ", genres=" + genres + "]";
	}
	
	

}
