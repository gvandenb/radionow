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
@Document(indexName = "search_customers", writeTypeHint = WriteTypeHint.FALSE)
public class SearchUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(type = FieldType.Text, name = "first_name")
	private String firstName;
	
	@Field(type = FieldType.Text, name = "last_name")
	private String lastName;
	
	@Field(type = FieldType.Text, name = "email")
	private String email;
	
	@Field(type = FieldType.Text, name = "phone_number")
	private String phoneNumber;
	
	@Builder.Default
	@Field(type = FieldType.Nested, name = "statistic")
	private SearchStatistic statistic = new SearchStatistic(null, 1L, StatisticType.USER);
	
	@Builder.Default
	@Field(type = FieldType.Nested, name = "devices")
	private List<String> devices = new ArrayList<String>();
	
	
}
