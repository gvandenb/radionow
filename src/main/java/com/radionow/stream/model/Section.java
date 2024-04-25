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


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sections")
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 2048, name = "headerName")
    private String headerName;
	
	@Column(name = "categoryName")
	private String categoryName;
	
	@Column(name = "sortOrder")
	private Integer sortOrder;

	@Builder.Default
	@Column(name = "sectionType")
	private SectionType sectionType = SectionType.STANDARD;
	
	@Column(name = "sortBy")
	private String sortBy;
	
	public static enum SectionType {
		STANDARD, TOP_10, TOP_20, TOP_50, TOP_100
	}
}
