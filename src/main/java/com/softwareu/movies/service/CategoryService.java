package com.softwareu.movies.service;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Category;
import com.softwareu.movies.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category save(Category category);

    Optional<Category> findById(Long id);

    List<CategoryDTO> findAll();

    void delete(Long id);

    Page<MovieDTO> findMoviesByCategoryName(String name, Pageable page);

	CategoryDTO save(CategoryDTO newCategory);
}
