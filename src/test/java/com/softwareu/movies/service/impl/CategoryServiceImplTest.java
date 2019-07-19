package com.softwareu.movies.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.softwareu.movies.converter.Category2CategoryDTO;
import com.softwareu.movies.converter.CategoryDTO2Category;
import com.softwareu.movies.repository.CategoryRepository;
import com.softwareu.movies.service.MovieService;

public class CategoryServiceImplTest {

	@Mock
	CategoryRepository categoryRepository;
	
	@Mock
	Category2CategoryDTO toCategoryDTO;
	
	@Mock
	CategoryDTO2Category toCategory;
	
	@Mock
	MovieService movieService;
	
	@InjectMocks
	CategoryServiceImpl categoryServiceImpl;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSaveCategory() {
		//given
		Category category = new Category("Drama");
		Category saved = new Category(1L, "Drama");
		when(categoryRepository.save(category)).thenReturn(saved);
		
		//when
		Category result = categoryServiceImpl.save(category);
		
		//then
		assertNotNull(result.getId());
		assertNotEquals(category.getId(), result.getId());
		assertEquals(saved.getName(), result.getName());
	}

	@Test
	public void testFindById() {
		//given
		Category c = new Category(1L, "Drama");
		Optional<Category> optionalCategory = Optional.of(c);
		when(categoryRepository.findById(c.getId())).thenReturn(optionalCategory);
		
		//when
		Optional<Category> result = categoryServiceImpl.findById(c.getId());
		
		//then
		assertEquals(c.getId(), result.get().getId());
		assertEquals(c.getName(), result.get().getName());
	}

	@Test
	public void testFindAll() {
		//given
		Category drama = new Category("Drama");
		Category action = new Category("Action");
		List<Category> categories = Arrays.asList(drama, action);
		CategoryDTO dramaDTO = new CategoryDTO(null,"Drama");
		CategoryDTO actionDTO = new CategoryDTO(null, "Action");
		List<CategoryDTO> dtosList = Arrays.asList(dramaDTO, actionDTO);
		when(categoryRepository.findAll()).thenReturn(categories);
		when(toCategoryDTO.convertWithoutMovies(categories)).thenReturn(dtosList);
		
		//when
		List<CategoryDTO> result = categoryServiceImpl.findAll();
		
		//then
		assertNotNull(result);
		assertEquals(drama.getName(), result.get(0).getName());
		assertEquals(action.getName(), result.get(1).getName());
	}

	@Test
	public void testDelete() {
		Long id = 1L;
		
		categoryServiceImpl.delete(id);
		
		verify(categoryRepository, times(1)).deleteById(id);
	}

	@Test
	public void testFindMoviesByCategoryName() {
		//given
		String name = "Drama";
		Pageable page = PageRequest.of(0, 10);
		MovieDTO dto1 = new MovieDTO("id1", "godfather", 1971, null);
		MovieDTO dto2 = new MovieDTO("id2", "scream", 2001, null);
		Page<MovieDTO> movies = new PageImpl<>(Arrays.asList(dto1, dto2));
		
		when(movieService.findByCategoryName(name, page)).thenReturn(movies);
		
		//when
		Page<MovieDTO> result = categoryServiceImpl.findMoviesByCategoryName(name, page);
		
		//then
		assertNotNull(result);;
		assertEquals(dto1.getImdbId(), result.getContent().get(0).getImdbId());
		assertEquals(dto1.getTitle(), result.getContent().get(0).getTitle());
		assertEquals(dto1.getYear(), result.getContent().get(0).getYear());
		assertEquals(dto2.getImdbId(), result.getContent().get(1).getImdbId());
		assertEquals(dto2.getTitle(), result.getContent().get(1).getTitle());
		assertEquals(dto2.getYear(), result.getContent().get(1).getYear());
	}

	@Test
	public void testSaveCategoryDTO() {
		//given
		CategoryDTO dramaDTO = new CategoryDTO(null,"Drama");
		Category converted = new Category("Drama");
		Category saved = new Category(1L, "Drama");
		CategoryDTO savedDramaDTO = new CategoryDTO(1L, "Drama");
		when(toCategory.convert(dramaDTO)).thenReturn(converted);
		when(categoryRepository.save(converted)).thenReturn(saved);
		when(toCategoryDTO.convert(saved)).thenReturn(savedDramaDTO);
		
		//when
		CategoryDTO result = categoryServiceImpl.save(dramaDTO);
		
		//then
		assertNotNull(result);
		assertEquals(saved.getId(), result.getId());
		assertNotEquals(dramaDTO.getId(), result.getId());
		assertEquals(saved.getName(), result.getName());
	}

}
