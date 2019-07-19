package com.softwareu.movies.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable{

	private static final long serialVersionUID = 3815251293722223034L;
	private Long id;
	@NotNull
	@NotEmpty
	private String name;
	private List<MovieDTO> movies;
	private List<SubcategoryDTO> subcategories;
	
	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
}