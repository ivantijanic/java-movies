package com.softwareu.movies.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.helper.TestUtil;
import com.softwareu.movies.model.Category;
import com.softwareu.movies.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softwareu.movies.converter.Category2CategoryDTO;

public class CategoryControllerTest {
	
	@Mock
    CategoryService categoryService;
	
	@Mock
	Category2CategoryDTO toCategoryDTO;
	
	@InjectMocks
	CategoryController categoryController;
	
	MockMvc mockMvc;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
	}

	@Test
	public void testFindAll() throws Exception {
		CategoryDTO drama = new CategoryDTO(1L, "Drama");
		CategoryDTO action = new CategoryDTO(2L, "Action");
		List<CategoryDTO> categories = new ArrayList<>(Arrays.asList(drama, action));
		
		when(categoryService.findAll()).thenReturn(categories);
		
		mockMvc.perform(get("/api/categories"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].id", is(1)))
		.andExpect(jsonPath("$[0].name", is(drama.getName())))
		.andExpect(jsonPath("$[1].id", is(2)))
		.andExpect(jsonPath("$[1].name", is(action.getName())));
	}

	@Test
	public void testCreateCategory() throws Exception{
		CategoryDTO dramaDTO = new CategoryDTO(null,"Drama");
		Category converted = new Category("Drama");
		Category saved = new Category(1L, "Drama");
		CategoryDTO savedDramaDTO = new CategoryDTO(1L, "Drama");
		
		when(categoryService.save(dramaDTO)).thenReturn(savedDramaDTO);
		when(toCategoryDTO.convert(saved)).thenReturn(savedDramaDTO);
		
		mockMvc.perform(post("/api/categories")
				.content(TestUtil.convertObjectToJsonBytes(dramaDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("id", is(1)))
		.andExpect(jsonPath("name", is(savedDramaDTO.getName())));
	}

	@Test
	public void testDeleteCategory() throws Exception{
		Category drama = new Category(1L, "Drama");
		Optional<Category> categoryOptional =  Optional.of(drama);
		
		when(categoryService.findById(drama.getId())).thenReturn(categoryOptional);
		
		mockMvc.perform(delete("/api/categories/1"))
		.andExpect(status().isNoContent());
	}

	@Test
	public void testUpdate() throws Exception{
		CategoryDTO dramaDTO = new CategoryDTO(null,"Drama");
//		Category converted = new Category("Drama");
		Category updated = new Category(1L, "Drama");
		CategoryDTO updatedDramaDTO = new CategoryDTO(1L, "Drama");
		
		when(categoryService.save(dramaDTO)).thenReturn(updatedDramaDTO);
		when(toCategoryDTO.convert(updated)).thenReturn(dramaDTO);
		
		mockMvc.perform(put("/api/categories/1")
				.content(TestUtil.convertObjectToJsonBytes(dramaDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("id", is(1)))
		.andExpect(jsonPath("name", is(dramaDTO.getName())));
	}

	@Test
	public void testGetMoviesByCategory() throws Exception {
		MovieDTO dto1 = new MovieDTO("id1", "godfather", 1971, null);
		MovieDTO dto2 = new MovieDTO("id2", "scream", 2001, null);
		Page<MovieDTO> movies = new PageImpl<>(Arrays.asList(dto1, dto2));
		
		when(categoryService.findMoviesByCategoryName("drama", PageRequest.of(0, 10))).thenReturn(movies);
		
		mockMvc.perform(get("/api/categories/drama/movies"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].imdbId", is(dto1.getImdbId())))
		.andExpect(jsonPath("$[0].title", is(dto1.getTitle())))
		.andExpect(jsonPath("$[0].year", is(dto1.getYear())))
		.andExpect(jsonPath("$[1].imdbId", is(dto2.getImdbId())))
		.andExpect(jsonPath("$[1].title", is(dto2.getTitle())))
		.andExpect(jsonPath("$[1].year", is(dto2.getYear())));
		
	}

}
