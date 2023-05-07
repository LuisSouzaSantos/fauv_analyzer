package com.fauv.analyzer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {

	private String baseParserUrl;
	private String baseAuthUrl;
	
	public Properties(
			@Value("${application.http.parser}") String baseParserUrl, 
			@Value("${application.http.auth}") String baseAuthUrl) {
		this.baseParserUrl = baseParserUrl;
		this.baseAuthUrl = baseAuthUrl;
	}

	public String getBaseParserUrl() {
		return baseParserUrl;
	}
	
	public void setBaseParserUrl(String baseParserUrl) {
		this.baseParserUrl = baseParserUrl;
	}
	
	public String getBaseAuthUrl() {
		return baseAuthUrl;
	}
	
	public void setBaseAuthUrl(String baseAuthUrl) {
		this.baseAuthUrl = baseAuthUrl;
	}
	
}
