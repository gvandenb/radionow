package com.radionow.stream.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "episodes")
public class Episode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
    private String title;

	@Column(name = "pubDate")
	private Date pubDate;

	@Column(name = "duration")
	private Long duration;
	
	@Column(columnDefinition = "TEXT", name = "description")
	private String description;
	
	@Column(length = 2048, name = "remoteURL")
	private String remoteURL;
    
	@Column(length = 2048, name = "localFilePath")
	private String localFilePath;
	

	public Episode() {}

	public Episode(String title, Date pubDate, Long duration, String description, String imageURL,
			String remoteURL, String localFilePath) {
		super();
		this.title = title;
		this.pubDate = pubDate;
		this.duration = duration;
		this.description = description;
		this.remoteURL = remoteURL;
		this.localFilePath = localFilePath;
	}

	public Episode(Long id, String title, Date pubDate, Long duration, String description,
			String imageURL, String remoteURL, String localFilePath) {
		super();
		this.id = id;
		this.title = title;
		this.pubDate = pubDate;
		this.duration = duration;
		this.description = description;
		this.remoteURL = remoteURL;
		this.localFilePath = localFilePath;
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

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getRemoteURL() {
		return remoteURL;
	}

	public void setRemoteURL(String remoteURL) {
		this.remoteURL = remoteURL;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
