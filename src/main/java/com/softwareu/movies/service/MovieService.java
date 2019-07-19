package com.softwareu.movies.service;


import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
	
	Page<Movie> findAll(Pageable page);

	MovieDTO findOne(String id);

	MovieDTO findByTitle(String title);
	
	Movie save(Movie movie);

	MovieDTO save(MovieDTO movieDTO, Image image);

	void delete(Movie movie);
	
	void delete(String id);
	
	Page<MovieDTO> findByCategoryName(String name, Pageable page);

}
