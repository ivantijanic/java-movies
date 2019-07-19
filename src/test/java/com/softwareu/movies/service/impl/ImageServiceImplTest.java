package com.softwareu.movies.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.softwareu.movies.converter.Movie2MovieDTO;
import com.softwareu.movies.repository.ImageRepository;
import com.softwareu.movies.repository.MovieRepository;

public class ImageServiceImplTest {
	
	@Mock
	ImageRepository imageRepository;
	
	@Mock
	MovieRepository movieRepository;
	
	@Mock
	Movie2MovieDTO toMovieDTO;
	
	@InjectMocks
	ImageServiceImpl imageServiceImpl;
	
	static final String PATH = "c:\\temp";
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindImageById() {
		Long id = 1L;
		Image image = new Image();
		image.setId(1L);
		image.setImagePath(PATH);
		Optional<Image> optionalImage = Optional.of(image);
		
		when(imageRepository.findById(id)).thenReturn(optionalImage);
		
		Image result = imageServiceImpl.findImageById(id);
		
		assertEquals(id, result.getId());
		assertEquals(image.getImagePath(), result.getImagePath());
	}

	@Test
	public void testGetImageObj() {
		Image image = new Image(PATH);
		image.setId(1L);
		MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[10]);
		
		Image result = imageServiceImpl.getImageObj(file, PATH);
		
		assertEquals(image.getImagePath(), result.getImagePath());
	}


	@Test
	public void testAddImage() {
		//given
		MovieDTO m1 = new MovieDTO("id1", "godfather", 1971, null);
		Movie found = new Movie("id1", "godfather", 1971, null);
		Movie savedMovie = new Movie("id1", "godfather", 1971, null);
		Image image = new Image();
		image.setId(1L);
		savedMovie.setImages(new ArrayList<Image>(Arrays.asList(image)));
		MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[10]);
		
		
		Image saved = new Image();
		saved.setId(1L);
		when(imageRepository.save(image)).thenReturn(saved);
		
		when(movieRepository.findById(m1.getImdbId())).thenReturn(Optional.of(found));
		when(movieRepository.save(found)).thenReturn(savedMovie);
		
		//when
		String imageUrl = imageServiceImpl.addImage(m1, file, PATH);
		
		
		//TODO needs to be tested better
		assertNotNull(imageUrl);
		
	}

	@Test
	public void testGetImageUrl() {
		//given
		Image image = new Image();
		image.setId(1L);
		
		//when
		String result = imageServiceImpl.getImageUrl(image);
		
		//then
		assertEquals("/api/images/1", result);
		
	}

}
