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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
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
@Table(name = "books")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 1024, name = "title")
	private String title;
	
	@Builder.Default
	@Column(length = 1024, name = "guid")
	private String guid = UUID.randomUUID().toString();
	
	@Column(length = 8192, name = "description")
	private String description;
	
	@Column(name = "language")
	private String language;
	
	@Column(name = "copyright_year")
	private Integer copyrightYear;
	
	@Column(length = 2048, name = "feed_url")		// URL to the RSS feed
	private String feedUrl;
	
	@Column(length = 2048, name = "project_url")	// URL to wikipedia
	private String projectUrl;
	
	@Column(length = 2048, name = "download_url")	// URL to download the archive (zip)
	private String downloadUrl;
	
	@Column(length = 2048, name = "info_url")		// URL to the librivox info web page
	private String infoUrl;
	
	@Column(length = 2048, name = "image_url")		// URL to the artwork image
	private String imageUrl;
	
	@Column(name = "total_time_seconds")
	private Long totalTimeSeconds;
	
	
	@Builder.Default
	@Column(name = "authors")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Author> authors = new ArrayList<Author>();
	
	@Builder.Default
	@Column(name = "categories")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Category> categories = new ArrayList<Category>();

	@Builder.Default
	@Column(name = "chapters")
	@OrderBy(value = "id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Chapter> chapters = new ArrayList<Chapter>();
    
    @Builder.Default
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "books_statistics", 
      joinColumns = 
        { @JoinColumn(name = "books_id", referencedColumnName = "id") },
      inverseJoinColumns = 
        { @JoinColumn(name = "statistics_id", referencedColumnName = "id") })
	private Statistic statistic = new Statistic();
    
	@Column(name = "created_at")
	@CreationTimestamp
    private Date createdAt;
    
	@Column(name = "updated_at")
	@UpdateTimestamp
    private Date updatedAt;
	
	
}
