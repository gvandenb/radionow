package com.radionow.stream.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.radionow.stream.data.GraphqlRequestBody;
import com.radionow.stream.data.PodcastGraphqlDto;
import com.radionow.stream.util.GraphqlSchemaReaderUtil;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
@Slf4j
public class PodcastClient {

	private final String url;

	public PodcastClient(@Value("https://api.taddy.org") String url) {
		this.url = url;
		System.out.println("Graphql API URL: " + url);
	}

	public PodcastGraphqlDto getTopChartsByGenres(String genre, Integer page, Integer size) throws IOException {
		HttpClient httpClient = HttpClient.create()
			      .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
		ReactorClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);  
			
		WebClient webClient = WebClient.builder().clientConnector(conn).build();

		GraphqlRequestBody graphQLRequestBody = new GraphqlRequestBody();

		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getTopChartsByGenres");
		String genreToken = query.replace("{genre}", genre);
		String pageToken = genreToken.replace("{page}", page.toString());
		String tokenizedQuery = pageToken.replace("{size}", size.toString());
		//final String variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("variables");

		graphQLRequestBody.setQuery(tokenizedQuery);
		//graphQLRequestBody.setVariables(variables.replace("countryCode", countryCode));
		System.out.println("Query: " + query);

		

		PodcastGraphqlDto p = webClient
				.post()
				.uri(url)
				.header("X-USER-ID", "1360")
				.header("X-API-KEY", "0eb4f8a9a0386ba205cd7d8321703ac354efd0e5fb18b55e81942d18228a1103c3cd33da4551d56aa7066afdd2ef1fe34c")
				.bodyValue(graphQLRequestBody)
				.retrieve()
				.bodyToMono(PodcastGraphqlDto.class)
				.block();
		
		
		System.out.println("P: " + p);
		return p;
	}
}
