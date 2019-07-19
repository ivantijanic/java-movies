package com.softwareu.movies.controller;

import java.util.List;

import javax.validation.Valid;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Category;
import com.softwareu.movies.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDTO>> findAll() {
        return new ResponseEntity<List<CategoryDTO>>(categoryService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO newCategory) {
        CategoryDTO created = categoryService.save(newCategory);
        return new ResponseEntity<>(created, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
    	Category category = categoryService.findById(id).get();
        categoryService.delete(category.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> update(@RequestBody @Valid CategoryDTO category,
                                          @PathVariable("categoryId") Long categoryId) {
    	CategoryDTO updated = categoryService.save(category);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @RequestMapping(value = "/{category}/movies", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@PathVariable("category") String category,
                                                              @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    	Page<MovieDTO> movies = categoryService.findMoviesByCategoryName(category, PageRequest.of(page, size));
        return new ResponseEntity<>(movies.getContent(), HttpStatus.OK);
    }
}
