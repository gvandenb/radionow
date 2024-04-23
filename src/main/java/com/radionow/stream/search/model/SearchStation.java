package com.radionow.stream.search.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import com.radionow.stream.search.model.SearchStatistic.StatisticType;

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
@Document(indexName = "search_stations", writeTypeHint = WriteTypeHint.FALSE)
public class SearchStation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(type = FieldType.Text, name = "guid")
	private String guid;
	
	@Field(type = FieldType.Text, name = "title")
	private String title;

	@Field(type = FieldType.Text, name = "callsign")
	private String callsign;
	
	@Field(type = FieldType.Text, name = "frequency")
	private String frequency;
	
	@Field(type = FieldType.Text, name = "description")
	private String description;
	
	@Field(type = FieldType.Text, name = "url")
	private String url;
	
	@Field(type = FieldType.Text, name = "city")
	private String city;
	
	@Field(type = FieldType.Text, name = "state")
	private String state;
	
	@Field(type = FieldType.Text, name = "country")
	private String country;
	
	@Field(type = FieldType.Text, name = "language")
	private String language;
	
	@Field(type = FieldType.Text, name = "imageUrl")
	private String imageUrl;
	
	@Field(type = FieldType.Boolean, name = "published")
	private Boolean published;

	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private List<SearchCategory> categories = new ArrayList<SearchCategory>();
	
	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private SearchStatistic statistic = new SearchStatistic(null, 1L, 1, 1, StatisticType.STATION);
	

	
	

}
