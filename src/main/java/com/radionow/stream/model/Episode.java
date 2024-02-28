package com.radionow.stream.model;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "episodes")
public class Episode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
    private String title;
	
	@Column(name = "guid")
    private String guid;

	@Column(name = "pubDate")
	private Date pubDate;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "podcast_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Podcast podcast;

	@Column(name = "duration")
	private Long duration;
	
	@Column(columnDefinition = "TEXT", name = "description")
	private String description;
	
	@Column(length = 2048, name = "remoteURL")
	private String remoteURL;
    
	@Column(length = 2048, name = "localFilePath")
	private String localFilePath;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "episodes_statistics", 
      joinColumns = 
        { @JoinColumn(name = "episodes_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "statistics_id", referencedColumnName = "id") })
	private Statistic statistic = new Statistic(1L, StatisticType.EPISODE);
	

	public Episode() {}

	public Episode(String title, String guid, Date pubDate, Long duration, String description, String imageURL,
			String remoteURL, String localFilePath) {
		super();
		this.title = title;
		this.guid = guid;
		this.pubDate = pubDate;
		this.duration = duration;
		this.description = description;
		this.remoteURL = remoteURL;
		this.localFilePath = localFilePath;
	}

	public Episode(Long id, String title, String guid, Date pubDate, Long duration, String description,
			String imageURL, String remoteURL, String localFilePath) {
		super();
		this.id = id;
		this.title = title;
		this.guid = guid;
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

	public Podcast getPodcast() {
		return podcast;
	}

	public void setPodcast(Podcast podcast) {
		this.podcast = podcast;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Statistic getStatistic() {
		return statistic;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}
	
	
	
}
