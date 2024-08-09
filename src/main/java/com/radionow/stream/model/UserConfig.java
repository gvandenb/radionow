package com.radionow.stream.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_configs")
public class UserConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "userId")
	private Long userId;

	@Builder.Default
	@Column(name = "podcastSections")
	@OrderBy(value = "sortOrder")
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Section> podcastSections = new ArrayList<Section>();

	@Builder.Default
	@Column(name = "stationSections")
	@OrderBy(value = "sortOrder")
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Section> stationSections = new ArrayList<Section>();
	
	@Builder.Default
	@Column(name = "audiobookSections")
	@OrderBy(value = "sortOrder")
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Section> audiobookSections = new ArrayList<Section>();
	

	
}

