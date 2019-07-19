package com.softwareu.movies.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import com.softwareu.movies.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Movie2MovieDTO implements Converter<Movie, MovieDTO>{

	@Autowired
	private Category2CategoryDTO toCategoryDTO;
	
	@Autowired
	private ImageService imageService;
	
	@Override
	public MovieDTO convert(Movie source) {
		MovieDTO target = new MovieDTO(source.getImdbId(), source.getTitle(),
				source.getYear(), source.getDescription());
		target.setCategories(toCategoryDTO.convertWithoutMovies(source.getCategories()));
		target.setImageUrls(getImageUrls(source.getImages()));
		return target;
	}
	
	public List<MovieDTO> convert(List<Movie> source) {
		return source.stream().map(x -> convert(x)).collect(toList()); 
	}

	private List<String> getImageUrls(List<Image> images) {
		return images.stream().map(x -> imageService.getImageUrl(x)).collect(toList());
	}


	
}


