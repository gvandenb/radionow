package com.radionow.stream.search.model;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchStationResult {
	
	private Long totalElements = 0L;
	private List<SearchStation> content = new ArrayList<SearchStation>();
	
	
}
