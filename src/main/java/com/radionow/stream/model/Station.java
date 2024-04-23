package com.radionow.stream.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*; // for Spring Boot 3
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
	
	@Builder.Default
	@NaturalId
	@Column(name = "guid")
	private String guid = UUID.randomUUID().toString();
	
	@Column(length = 4096, name = "url")
	private String url;
	
	@Builder.Default
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
	
	@Column(name = "published")
	private Boolean published;
	
	@Column(name = "language")
	private String language;
	
	@Column(name = "created_at")
	@CreationTimestamp
    private Date createdAt;
    
	@Column(name = "updated_at")
	@UpdateTimestamp
    private Date updatedAt;

	
	

}
