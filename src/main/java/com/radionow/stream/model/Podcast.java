package com.radionow.stream.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "podcasts", indexes = { @Index(name = "podcast_feedurl_idx", columnList = "feedurl")})
public class Podcast {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
    private String title;
	
	@Builder.Default
	@Column(name = "guid")
	private String guid = UUID.randomUUID().toString();
	
	@Column(name = "author")
	private String author;
	
	@Column(length = 4096, name = "description")
	private String description;
	
	@Column(length = 2048, name = "feedURL")
	private String feedURL;
	
	@Column(length = 2048, name = "artworkURL")
	private String artworkURL;
	
	@Column(name = "rank")
	private Integer rank;
	
	@Builder.Default
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "podcast", cascade = CascadeType.ALL)
	private List<Episode> episodes = new ArrayList<Episode>();
	
	@Builder.Default
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
	
	@Column(name = "lastPubDate")
	private Date lastPubDate;
	
	@Column(name = "created_at")
	@CreationTimestamp
    private Date createdAt;
    
	@Column(name = "updated_at")
	@UpdateTimestamp
    private Date updatedAt;

}
