package com.softwareu.movies.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.softwareu.movies.dto.SubcategoryDTO;
import com.softwareu.movies.model.Subcategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubcategoryDTO2Subcategory implements Converter<SubcategoryDTO, Subcategory> {

	
	@Override
	public Subcategory convert(SubcategoryDTO source) {
		Subcategory target = new Subcategory(source.getId(), source.getName(), null);
		return target;
	}

	public List<Subcategory> convert(List<SubcategoryDTO> source) {
		return source.stream().map(x -> convert(x)).collect(toList());
	}
}
