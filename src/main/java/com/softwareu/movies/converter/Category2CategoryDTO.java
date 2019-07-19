package com.softwareu.movies.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Category2CategoryDTO implements Converter<Category, CategoryDTO> {

	@Autowired
	private Movie2MovieDTO toMovieDTO;
	
	@Autowired
	private Subcategory2SubcategoryDTO toSubcategoryDTO;
	
	@Override
	public CategoryDTO convert(Category source) {
		CategoryDTO target = convertWithoutMovies(source);
		target.setMovies(toMovieDTO.convert(source.getMovies()));
		
		return target;
	}
	
	public CategoryDTO convertWithoutMovies(Category source) {
		CategoryDTO target = new CategoryDTO(source.getId(), source.getName());
		target.setSubcategories(toSubcategoryDTO.convert(source.getSubcategories()));
		return target;
	}
	
	public List<CategoryDTO> convert(List<Category> categories) {
		return categories.stream().map(x -> convert(x)).collect(toList());
	}

	public List<CategoryDTO> convertWithoutMovies(List<Category> categories) {
		return categories.stream().map(x -> convertWithoutMovies(x)).collect(toList());
	}
}
