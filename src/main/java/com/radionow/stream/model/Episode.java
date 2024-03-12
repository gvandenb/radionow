package com.radionow.stream.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
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
  
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "podcast_id")
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
	private Statistic statistic;
	

	
	
	
}
