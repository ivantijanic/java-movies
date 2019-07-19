package com.softwareu.movies.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.helper.ImdbIdUtil;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.softwareu.movies.converter.Movie2MovieDTO;
import com.softwareu.movies.converter.MovieDTO2Movie;
import com.softwareu.movies.exception.NotFoundException;
import com.softwareu.movies.repository.ImageRepository;
import com.softwareu.movies.repository.MovieRepository;

public class MovieServiceImplTest {

	@Mock
	MovieRepository movieRepository;

	@Mock
	MovieDTO2Movie toMovie;

	@Mock
	Movie2MovieDTO toMovieDTO;
	
	@Mock
	ImageRepository imageRepository;
	
	@InjectMocks
	MovieServiceImpl movieServiceImpl;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindAll() {
		//given
		Movie m1 = new Movie("id1", "godfather", 1971, null);
		Movie m2 = new Movie("id2", "scream", 2001, null); 
		Page<Movie> movies = new PageImpl<>(Arrays.asList(m1, m2));
		PageRequest pageRequest = PageRequest.of(0, 10);
		when(movieRepository.findAll(pageRequest)).thenReturn(movies);
		
		//when
		Page<Movie> returned = movieServiceImpl.findAll(pageRequest);
		
		//then
		assertEquals(movies, returned);
	}

	@Test
	public void testFindOne() {
		//given
		Movie m = new Movie("id1", "godfather", 1971, null);
		MovieDTO dto = new MovieDTO("id1", "godfather", 1971, null);
		Optional<Movie> optionalMovie = Optional.of(m);
		when(movieRepository.findById(m.getImdbId())).thenReturn(optionalMovie);
		when(toMovieDTO.convert(m)).thenReturn(dto);
		
		//when
		MovieDTO result = movieServiceImpl.findOne(m.getImdbId());
		
		//then
		assertEquals(m.getImdbId(), result.getImdbId());
		assertEquals(m.getTitle(), result.getTitle());
		assertEquals(m.getYear(), result.getYear());
		assertEquals(m.getDescription(), result.getDescription());
		
	}
	
	@Test(expected = NotFoundException.class)
	public void whenSearchingNonExistingMovie_shouldThrowNotFoundExecption() {
		String nonExistingId = "tt0334545";
		
		when(movieRepository.findById(nonExistingId)).thenReturn(Optional.ofNullable(null));
		
		MovieDTO result = movieServiceImpl.findOne(nonExistingId);
		
		assertNull(result);
		
	}

	@Test
	public void testFindByTitle() {
		//given
		Movie m = new Movie("id1", "godfather", 1971, null);
		MovieDTO dto = new MovieDTO("id1", "godfather", 1971, null);
		String title = "godfather";
		
		when(movieRepository.findByTitle(title)).thenReturn(Optional.of(m));
		when(toMovieDTO.convert(m)).thenReturn(dto);
		
		//when
		MovieDTO result = movieServiceImpl.findByTitle(title);
		
		//then
		assertEquals(dto.getImdbId(), result.getImdbId());
		assertEquals(dto.getTitle(), result.getTitle());
		assertEquals(dto.getYear(), result.getYear());
		
	}

	@Test
	public void testSaveMovieDTO() {
		//given
		MovieDTO dto = new MovieDTO("godfather", 1971);
		Movie m = new Movie(null, "godfather", 1971, null);
		MovieDTO saved = new MovieDTO(ImdbIdUtil.randomImdbId(), dto.getTitle(), dto.getYear(), null);
		Image image = null;
		when(movieServiceImpl.save(dto, image)).thenReturn(saved);
		when(toMovie.convert(dto)).thenReturn(m);
		when(imageRepository.save(image)).thenReturn(image);
		
		when(toMovieDTO.convert(m)).thenReturn(saved);
		
		//when
		MovieDTO result = movieServiceImpl.save(dto, image);
		
		//then
		assertNotNull(result.getImdbId());
		assertEquals(saved.getImdbId(), result.getImdbId());
		assertEquals(saved.getTitle(), result.getTitle());
		assertEquals(saved.getYear(), result.getYear());
		
		
	}


	@Test
	public void testDeleteMovie() {
		//given
		Movie m = new Movie("id1", "godfather", 1971, null);
		
		//when
		movieServiceImpl.delete(m);
		
		//then
		verify(movieRepository, times(1)).delete(m);
	}

	@Test
	public void testDeleteById() {
		//given
		Movie m = new Movie("id1", "godfather", 1971, null);
		
		//when
		movieServiceImpl.delete(m.getImdbId());
		
		//then
		verify(movieRepository, times(1)).deleteById(m.getImdbId());
	}
	
	@Test
	public void testFindByCategoryName() {
		//given
		String categoryName = "Drama";
		Pageable page = PageRequest.of(0, 10);
		Movie m1 = new Movie("id1", "godfather", 1971, null);
		Movie m2 = new Movie("id2", "scream", 2001, null);
		Page<Movie> moviesPage = new PageImpl<>(Arrays.asList(m1, m2));
		MovieDTO dto1 = new MovieDTO("id1", "godfather", 1971, null);
		MovieDTO dto2 = new MovieDTO("id2", "scream", 2001, null);
		List<MovieDTO> dtosList = Arrays.asList(dto1, dto2);
		when(movieRepository.findByCategoryName(categoryName, page)).thenReturn(moviesPage);
		when(toMovieDTO.convert(moviesPage.getContent())).thenReturn(dtosList);
		
		//when
		Page<MovieDTO> result = movieServiceImpl.findByCategoryName(categoryName, page);
		
		//then
		assertNotNull(result);
		assertEquals(result.getContent().size(), dtosList.size());
		assertEquals(result.getContent().get(0).getTitle(), dtosList.get(0).getTitle());
		assertEquals(result.getContent().get(0).getImdbId(), dtosList.get(0).getImdbId());
		assertEquals(result.getContent().get(1).getTitle(), dtosList.get(1).getTitle());
		assertEquals(result.getContent().get(1).getImdbId(), dtosList.get(1).getImdbId());
	}

}
