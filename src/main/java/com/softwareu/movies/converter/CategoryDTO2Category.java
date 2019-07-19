package com.softwareu.movies.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTO2Category implements Converter<CategoryDTO, Category> {

	@Autowired
	private MovieDTO2Movie toMovie;
	
	@Autowired
	private SubcategoryDTO2Subcategory toSubcategory;
	
	@Override
	public Category convert(CategoryDTO source) {
		Category target = convertWithoutMovies(source);
		if (source.getMovies() != null)
			target.setMovies(toMovie.convert(source.getMovies()));
		
		return target;
	}
	
	public Category convertWithoutMovies(CategoryDTO source) {
		Category target = new Category(source.getId(), source.getName());
		if (source.getSubcategories() != null)
			target.setSubcategories(toSubcategory.convert(source.getSubcategories()));
		return target;
	}
	
	public List<Category> convert(List<CategoryDTO> source) {
		return source.stream().map(x -> convertWithoutMovies(x)).collect(toList());
	}

}
