package com.radionow.stream.search.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import com.radionow.stream.search.model.SearchStatistic.StatisticType;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "search_audiobooks", writeTypeHint = WriteTypeHint.FALSE)
public class SearchAudiobook {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(type = FieldType.Text, name = "title")
	private String title;
	
	@Field(type = FieldType.Text, name = "guid")
	private String guid;
	
	@Field(type = FieldType.Text, name = "description")
	private String description;
	
	@Field(type = FieldType.Text, name = "language")
	private String language;
	
	@Field(type = FieldType.Integer, name = "copyrightYear")
	private Integer copyrightYear;
	
	@Field(type = FieldType.Text, name = "feedUrl")   		// URL to the RSS feed
	private String feedUrl;
	
	@Field(type = FieldType.Text, name = "projectUrl")		// URL to wikipedia
	private String projectUrl;
	
	@Field(type = FieldType.Text, name = "downloadUrl")	// URL to download the archive (zip)
	private String downloadUrl;
	
	@Field(type = FieldType.Text, name = "infoUrl")		// URL to the librivox info web page
	private String infoUrl;
	
	@Field(type = FieldType.Text, name = "imageUrl")		// URL to the artwork image
	private String imageUrl;
	
	@Field(type = FieldType.Long, name = "totalTimeSeconds")
	private Long totalTimeSeconds;
	
	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private List<SearchAuthor> authors = new ArrayList<SearchAuthor>();
	
	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private List<SearchCategory> categories = new ArrayList<SearchCategory>();

	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private List<SearchChapter> chapters = new ArrayList<SearchChapter>();
    
	@Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
	private SearchStatistic statistic = new SearchStatistic(null, 1L, 1, 1, StatisticType.AUDIOBOOK);
    
	@Field(type = FieldType.Date, name = "createdAt")
    private Date createdAt;
    
	@Field(type = FieldType.Date, name = "updatedAt")
    private Date updatedAt;
	
}
