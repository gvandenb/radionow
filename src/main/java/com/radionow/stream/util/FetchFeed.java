package com.radionow.stream.util;

import java.io.IOException;
import java.net.URL;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;




public class FetchFeed {

	
	public FetchFeed() {
		super();
	}
	
	public static SyndFeed readFeed(String url) throws IOException, FeedException {
        URL feedSource = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedSource));
    }
	

}
