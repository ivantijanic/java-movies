package com.softwareu.movies.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryDTO implements Serializable {

	private static final long serialVersionUID = 3897850578999416350L;
	private Long id;
	private String name;
	
}
