package com.radionow.stream.search.model;


import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import com.radionow.stream.search.model.SearchStatistic.StatisticType;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "search_podcasts", writeTypeHint = WriteTypeHint.FALSE)
public class SearchPodcast {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(type = FieldType.Long, name = "podcastId")
	private Long podcastId;
	
	@Field(type = FieldType.Text, name = "title")
    private String title;
	
	@Field(type = FieldType.Integer, name = "rank")
    private Integer rank;
	
	@Field(type = FieldType.Text, name = "guid")
    private String guid;
	
	@Field(type = FieldType.Text, name = "author")
	private String author;
	
	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private List<SearchCategory> categories = new ArrayList<SearchCategory>();
	
	@Field(type = FieldType.Text, name = "description")
	private String description;
	
	@Field(type = FieldType.Text, name = "feedURL")
	private String feedURL;
	
	@Field(type = FieldType.Text, name = "artworkURL")
	private String artworkURL;
	
	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private SearchStatistic statistic = new SearchStatistic(null, 1L, 1, 1, StatisticType.PODCAST);
    
	@Field(type = FieldType.Date, name = "lastPubDate")
	private Date lastPubDate;


}
