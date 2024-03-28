package com.radionow.stream.search.model;

import java.util.Date;

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
@Document(indexName = "search_episodes", writeTypeHint = WriteTypeHint.FALSE)
public class SearchEpisode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    @Field(type = FieldType.Nested, includeInParent = true)
	private SearchPodcast podcast;
	
	@Field(type = FieldType.Text, name = "title")
    private String title;
	
	@Field(type = FieldType.Text, name = "guid")
    private String guid;

	@Field(type = FieldType.Date, name = "pubDate")
	private Date pubDate;

	@Field(type = FieldType.Long, name = "duration")
	private Long duration;
	
	@Field(type = FieldType.Text, name = "description")
	private String description;
	
	@Field(type = FieldType.Text, name = "remoteURL")
	private String remoteURL;
    
	@Field(type = FieldType.Text, name = "localFilePath")
	private String localFilePath;

	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private SearchStatistic statistic = new SearchStatistic(null, 1L, StatisticType.STATION);

	
	
}
