package com.radionow.stream.search.model;



import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
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
@Document(indexName = "search_podcasts")
public class SearchPodcast {
	
	@Id
	private String id;
	
	@Field(type = FieldType.Long, name = "podcastId")
	private Long podcastId;
	
	@Field(type = FieldType.Text, name = "title")
    private String title;
	
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
	
	
    

}
