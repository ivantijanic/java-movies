package com.softwareu.movies.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import com.softwareu.movies.model.Category;
import com.softwareu.movies.model.Movie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MovieRepositoryTest {
	
	@Autowired
	MovieRepository repo;
	
	@Autowired
	CategoryRepository categoryRepo;

	@Before
	public void setUp() throws Exception {
		Category c = new Category("Drama");
		categoryRepo.save(c);
		Movie m1 = new Movie("id1", "godfather", 1971, null);
		m1.addCategory(c);
		Movie m2 = new Movie("id2", "scream", 2001, null); 
		m2.addCategory(c);
		repo.saveAll(Arrays.asList(m1, m2));
	}

	@Test
	public void testFindByTitle() {
		String title = "godfather";
		
		Movie result = repo.findByTitle(title).get();
		
		assertNotNull(result);
		assertEquals(title, result.getTitle());
	}

	@Test
	public void testFindByCategoryName() {
		String name = "Drama";
		
		Page<Movie> movies = repo.findByCategoryName(name, PageRequest.of(0, 10));
		
		assertNotNull(movies);
		assertEquals(2, movies.getContent().size());
	}
}
