package com.radionow.stream.search.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

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
@Document(indexName = "search_chapters", writeTypeHint = WriteTypeHint.FALSE)
public class SearchChapter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(type = FieldType.Text, name = "title")
    private String title;
	
	@Field(type = FieldType.Text, name = "guid")
    private String guid;
	
	@Field(type = FieldType.Text, name = "playUrl")
    private String playUrl;
	
	@Field(type = FieldType.Boolean, name = "explicit")
    private Boolean explicit;
	
	@Field(type = FieldType.Long, name = "duration")
    private Long duration;
	
}
