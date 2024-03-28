package com.radionow.stream.search.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "search_statistics", writeTypeHint = WriteTypeHint.FALSE)
public class SearchStatistic {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// clicked on a podcast, station or episode
	@Field(type = FieldType.Long, name = "views")
	private Long views;
	
	// clicked on a station
	@Field(type = FieldType.Nested, name = "statisticType")
	private StatisticType  statisticType;
	
	
	public static enum StatisticType {
		 USER, PODCAST, STATION, EPISODE
	}
	
	
}


