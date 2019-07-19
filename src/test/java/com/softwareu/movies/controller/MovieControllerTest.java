package com.softwareu.movies.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.softwareu.movies.dto.CategoryDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.helper.TestUtil;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import com.softwareu.movies.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softwareu.movies.converter.Movie2MovieDTO;
import com.softwareu.movies.service.MovieService;
import com.softwareu.movies.service.StorageService;

public class MovieControllerTest {
	
	@Mock
	MovieService movieService;
	
	@Mock
	Movie2MovieDTO toMovieDTO;
	
	@Mock
	StorageService storageService;
	
	@Mock
    ImageService imageService;
	
	@InjectMocks
	MovieController movieController;
	
	MockMvc mockMvc;
	
	String UPLOAD_DIR = "c:\\temp\\image\\";
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
	}
	
	@Test
	public void testListAll() throws Exception {

		Movie m1 = new Movie("id1", "godfather", 1971, null);
		Movie m2 = new Movie("id2", "scream", 2001, null);
		MovieDTO dto1 = new MovieDTO("id1", "godfather", 1971, null);
		MovieDTO dto2 = new MovieDTO("id2", "scream", 2001, null);
		Page<Movie> moviesPage = new PageImpl<>(Arrays.asList(m1, m2));
		List<MovieDTO> movieDTOS = new ArrayList<>();
		movieDTOS.add(dto1);
		movieDTOS.add(dto2);
		
		when(movieService.findAll(PageRequest.of(0, 10))).thenReturn(moviesPage);
		when(toMovieDTO.convert(moviesPage.getContent())).thenReturn(movieDTOS);
		
		mockMvc.perform(get("/api/movies").param("page", "0").param("size", "10"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].title", is(dto1.getTitle())))
		.andExpect(jsonPath("$[0].description", isEmptyOrNullString()))
		.andExpect(jsonPath("$[1].title", is(dto2.getTitle())))
		.andExpect(jsonPath("$[1].description", isEmptyOrNullString()));
		
	}

	@Test
	public void testFindOne() throws Exception {
		MovieDTO movie = new MovieDTO("id1", "godfather", 1971, null);
		
		when(movieService.findOne(movie.getImdbId())).thenReturn(movie);
		
		mockMvc.perform(get("/api/movies/id1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("year", is(movie.getYear())))
		.andExpect(jsonPath("title", is(movie.getTitle())));
	}

	@Test
	public void testFindByMovieName() throws Exception {
		MovieDTO movie = new MovieDTO("id1", "godfather", 1971, null);
		
		when(movieService.findByTitle("godfather")).thenReturn(movie);
		
		mockMvc.perform(get("/api/movies/name/godfather"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("year", is(movie.getYear())))
		.andExpect(jsonPath("title", is(movie.getTitle())));
		
	}

	
	@Test
	public void testCreate() throws Exception {
		CategoryDTO category = new CategoryDTO(1L, "Drama");
		List<CategoryDTO> categories = Arrays.asList(category);
		MovieDTO movie = new MovieDTO("id1", "godfather", 1971, null, categories);
		Image imageObj = new Image();
		MockMultipartFile movieJson = new MockMultipartFile("movie", "", "application/json", TestUtil.MOVIE_JSON.getBytes());
		MockMultipartFile image = new MockMultipartFile("file", "", "image/jpeg", new byte[10]);
	
		when(imageService.getImageObj(image, UPLOAD_DIR)).thenReturn(imageObj);
		when(movieService.save(movie, imageObj)).thenReturn(movie);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/movies").file(image).file(movieJson))

		.andExpect(status().isCreated());
	}

	@Test
	public void testUpdate() throws Exception {
		CategoryDTO category = new CategoryDTO(1L, "Drama");
		List<CategoryDTO> categories = Arrays.asList(category);
		MovieDTO movie = new MovieDTO("id1", "godfather", 1971, null, categories);
		
		when(movieService.save(movie, null)).thenReturn(movie);
	
		
		mockMvc.perform(put("/api/movies/id1")
				.content(TestUtil.convertObjectToJsonBytes(movie))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("imdbId", is(movie.getImdbId())))
		.andExpect(jsonPath("title", is(movie.getTitle())))
		.andExpect(jsonPath("year", is(movie.getYear())))
		.andExpect(jsonPath("categories", hasSize(1)));
	}

	@Test
	public void testDelete() throws Exception {
		MovieDTO movie = new MovieDTO("id1", "godfather", 1971, null);
		
		when(movieService.findOne("id1")).thenReturn(movie);
		
		mockMvc.perform(delete("/api/movies/id1"))
		.andExpect(status().isNoContent());
	}

}
