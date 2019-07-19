package com.softwareu.movies.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

	@Id
	@Column(name = "imdb_id")
	private String imdbId;
	private String title;
	private int year;
	private String description;

	@OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "movie_category", joinColumns =
	@JoinColumn(name = "movie_id", referencedColumnName = "imdb_id"), inverseJoinColumns =
	@JoinColumn(name = "category_id", referencedColumnName = "id"))
	private List<Category> categories = new ArrayList<>();

	public Movie(String imdbId, String title, int year, String description) {
		this.imdbId = imdbId;
		this.title = title;
		this.year = year;
		this.description = description;
	}

	public void addCategory(Category c) {
		categories.add(c);
		c.getMovies().add(this);
	}

	public Movie(String id, String title, int year, String description,List<Image> images) {
		this.imdbId = id;
		this.title = title;
		this.year = year;
		this.description = description;
		this.images = images;
	}

	public void addImage(Image image) {
		this.images.add(image);
		image.setMovie(this);
	}

}
