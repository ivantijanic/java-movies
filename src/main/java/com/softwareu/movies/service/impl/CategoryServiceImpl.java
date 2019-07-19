package com.softwareu.movies.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.softwareu.movies.converter.Category2CategoryDTO;
import com.softwareu.movies.converter.CategoryDTO2Category;
import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Category;
import com.softwareu.movies.repository.CategoryRepository;
import com.softwareu.movies.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.softwareu.movies.service.MovieService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CategoryDTO2Category toCategory;
    
    @Autowired
    private Category2CategoryDTO toCategoryDTO;
  
    @Autowired
    private MovieService movieService;

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Cacheable(value = "category")
    public List<CategoryDTO> findAll() {
    	List<Category> categories = (List<Category>) categoryRepository.findAll();
        return toCategoryDTO.convertWithoutMovies(categories);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Cacheable(value = "movie", key="#p0")
    public Page<MovieDTO> findMoviesByCategoryName(String name, Pageable page) {
    	return movieService.findByCategoryName(name, page);

    }
    
    @CacheEvict(value="category")
    public CategoryDTO save(CategoryDTO newCategory) {
    	Category saved = categoryRepository.save(toCategory.convert(newCategory));
    	return toCategoryDTO.convert(saved);
    }
}
