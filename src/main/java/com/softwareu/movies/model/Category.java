package com.softwareu.movies.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "movies")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@ManyToMany(mappedBy = "categories")
	private List<Movie> movies = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "category_subcategory", joinColumns = 
	@JoinColumn(name = "category_id", referencedColumnName = "id"), inverseJoinColumns = 
	@JoinColumn(name = "subcategory_id", referencedColumnName = "id"))
	private List<Subcategory> subcategories = new ArrayList<>();
	
	public Category(String name) {
		this.name = name;
	}
	
	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void addSubcategory(Subcategory s) {
		subcategories.add(s);
		s.getCategories().add(this);
	}

	
	
}
