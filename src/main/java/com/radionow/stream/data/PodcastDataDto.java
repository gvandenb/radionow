package com.radionow.stream.data;

import java.util.List;

import lombok.Data;

@Data
public class PodcastDataDto {

	private String topChartsId;
	private List<PodcastDto> podcastSeries;

}
