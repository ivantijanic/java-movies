package com.softwareu.movies.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.softwareu.movies.dto.SubcategoryDTO;
import com.softwareu.movies.model.Subcategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Subcategory2SubcategoryDTO implements Converter<Subcategory, SubcategoryDTO> {
	
	@Override
	public SubcategoryDTO convert(Subcategory source) {
		return new SubcategoryDTO(source.getId(), source.getName());
	}
	
	public List<SubcategoryDTO> convert(List<Subcategory> source) {
		return source.stream().map(x -> convert(x)).collect(toList());
	}

}
