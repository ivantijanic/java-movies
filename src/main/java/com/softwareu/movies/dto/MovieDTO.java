package com.softwareu.movies.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO implements Serializable{

	private static final long serialVersionUID = 5703515008687995966L;

	private String imdbId;
	
	@NotNull
	@NotEmpty
	private String title;
	
	@Min(1900)
	@Max(2020)
	@NotNull
	private int year;
	private String description;

	private List<String> imageUrls;
	@NotNull
	private List<CategoryDTO> categories;

	public MovieDTO(String id, String title, int year, String description) {
		this.imdbId = id;
		this.title = title;
		this.year = year;
		this.description = description;
	}

	public MovieDTO(String imdbId, String title, int year, String description, List<CategoryDTO> categories) {
		this.imdbId = imdbId;
		this.title = title;
		this.year = year;
		this.description = description;
		this.categories = categories;
	}

	public MovieDTO(String title, int year) {
		this.title = title;
		this.year = year;
	}
}
