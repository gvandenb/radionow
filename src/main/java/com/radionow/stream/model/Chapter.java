package com.radionow.stream.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 2048, name = "title")
    private String title;
	
	@Builder.Default
	@Column(name = "guid")
	private String guid = UUID.randomUUID().toString();
	
	@Column(length = 2048, name = "play_url")
    private String playUrl;
	
	@Column(name = "explicit")
    private Boolean explicit;
	
	@Column(name = "duration")
    private Long duration;
	
	
}
