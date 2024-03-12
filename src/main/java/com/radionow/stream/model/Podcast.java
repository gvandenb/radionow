package com.radionow.stream.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.radionow.stream.model.Statistic.StatisticType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "podcasts")
public class Podcast {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
    private String title;
	
	@Column(name = "author")
	private String author;
	
	@Column(length = 4096, name = "description")
	private String description;
	
	@Column(length = 2048, name = "feedURL")
	private String feedURL;
	
	@Column(length = 2048, name = "artworkURL")
	private String artworkURL;
	
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "podcast", cascade = CascadeType.ALL)
	private List<Episode> episodes = new ArrayList<Episode>();
	
	@Column(name = "categories")
	@OneToMany(cascade = CascadeType.ALL)
	private List<Category> categories = new ArrayList<Category>();
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "podcasts_statistics", 
      joinColumns = 
        { @JoinColumn(name = "podcasts_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "statistics_id", referencedColumnName = "id") })
	private Statistic statistic;
	
    
	public Podcast() {}
	

	public Podcast(String title, String author, String description, List<Episode> episodes, List<Category> categories,
			String feedURL, String artworkURL) {
		super();
		this.title = title;
		this.author = author;
		this.description = description;
		this.episodes = episodes;
		this.categories = categories;
		this.feedURL = feedURL;
		this.artworkURL = artworkURL;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeedURL() {
		return feedURL;
	}

	public void setFeedURL(String feedURL) {
		this.feedURL = feedURL;
	}

	public String getArtworkURL() {
		return artworkURL;
	}

	public void setArtworkURL(String artworkURL) {
		this.artworkURL = artworkURL;
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

	
	public List<Episode> getEpisodes() {
		return episodes;
	}


	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}


	@Override
	public String toString() {
		return "Podcast [id=" + id + ", title=" + title + ", author=" + author + ", description=" + description
				+ ", categories=" + categories + ", feedURL=" + feedURL + ", artworkURL="
				+ artworkURL + "]";
	}
	
	

}
