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
@Document(indexName = "search_authors", writeTypeHint = WriteTypeHint.FALSE)
public class SearchAuthor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(type = FieldType.Text, name = "firstName")
	private String firstName;
	
	@Field(type = FieldType.Text, name = "lastName")
	private String lastName;
	
	@Field(type = FieldType.Text, name = "dob")
	private String dob;
	
	@Field(type = FieldType.Text, name = "dod")
	private String dod;
	
}
