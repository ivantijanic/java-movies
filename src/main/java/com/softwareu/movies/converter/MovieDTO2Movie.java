package com.softwareu.movies.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieDTO2Movie implements Converter<MovieDTO, Movie> {

	@Autowired
	private CategoryDTO2Category toCategory;
	
	@Override
	public Movie convert(MovieDTO source) {
		Movie target = new Movie(source.getImdbId(), source.getTitle(), source.getYear(), source.getDescription());
		target.setCategories(toCategory.convert(source.getCategories()));
		return target;
	}

	public List<Movie> convert(List<MovieDTO> movies) {
		return movies.stream().map(x -> convert(x)).collect(toList());
	}

}
