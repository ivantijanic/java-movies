package com.softwareu.movies.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softwareu.movies.model.Category;
import com.softwareu.movies.model.Movie;
import com.softwareu.movies.model.Subcategory;
import com.softwareu.movies.repository.CategoryRepository;
import com.softwareu.movies.repository.MovieRepository;
import com.softwareu.movies.repository.SubcategoryRepository;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class InitData {
	
	@Autowired
	private MovieRepository movieRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private SubcategoryRepository subRepo;

	@PostConstruct
	public void init() {
		int i = 0;
    	Path path = FileSystems.getDefault().getPath("/upload-dir/ww.png");
		String defaultImage = path.toString();
		while (i < 200) {
			Subcategory s1 = new Subcategory("Subcategory " + i);
			Category c1 = new Category("Category " + i);
			Movie m1 = new Movie(ImdbIdUtil.randomImdbId(), "Movie " + i, 2016, null);
			c1.addSubcategory(s1);
			m1.addCategory(c1);
			subRepo.save(s1);
			categoryRepo.save(c1);
			movieRepo.save(m1);
			i++;
		}
	}
}
