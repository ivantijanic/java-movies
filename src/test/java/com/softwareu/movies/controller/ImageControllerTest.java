package com.softwareu.movies.controller;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.softwareu.movies.service.MovieService;
import com.softwareu.movies.service.StorageService;

public class ImageControllerTest {

	@Mock
    ImageService imageService;

	@Mock
	MovieService movieService;

	@Mock
	StorageService storageService;

	@InjectMocks
	ImageController imageController;

	MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
	}

	@Test
	public void testAddImage() throws Exception {
		String movieId = "id1";
		String imageUrl = "/api/images/1";
		MockMultipartFile image = new MockMultipartFile("file", "", "image/jpeg", new byte[10]);
		MovieDTO movie = new MovieDTO("id1", "godfather", 1971, null);
		
		when(movieService.findOne(movieId)).thenReturn(movie);
		when(imageService.addImage(movie, image, "")).thenReturn(imageUrl);
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images/id1").file(image))
		.andExpect(status().isCreated());
		
		verify(storageService, times(1)).store(image);
		
	}

}
