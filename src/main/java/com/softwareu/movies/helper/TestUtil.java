package com.softwareu.movies.helper;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
	
	public static final String MOVIE_JSON = "{\r\n" + 
			"    \"imdbId\": \"tt5555555\",\r\n" + 
			"    \"title\": \"Movie 4\",\r\n" + 
			"    \"year\": 2000,\r\n" + 
			"    \"description\": null,\r\n" + 
			"    \"imageUrls\": [\r\n" + 
			"        \"/api/images/1\"\r\n" + 
			"    ],\r\n" + 
			"    \"categories\": [\r\n" + 
			"        {\r\n" + 
			"            \"id\": 5,\r\n" + 
			"            \"name\": \"Category 4\",\r\n" + 
			"            \"movies\": null,\r\n" + 
			"            \"subcategories\": [\r\n" + 
			"                {\r\n" + 
			"                    \"id\": 5,\r\n" + 
			"                    \"name\": \"Subcategory 4\"\r\n" + 
			"                }\r\n" + 
			"            ]\r\n" + 
			"        }\r\n" + 
			"    ]\r\n" + 
			"}";
}
