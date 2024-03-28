package com.radionow.stream.search.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPodcastResult {

	private Long totalElements = 0L;
	private List<SearchPodcast> content = new ArrayList<SearchPodcast>();
	
}
