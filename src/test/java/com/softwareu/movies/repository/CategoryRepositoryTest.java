package com.softwareu.movies.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.softwareu.movies.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryRepositoryTest {
	
	@Autowired
	CategoryRepository repo;
	

	@Before
	public void setUp() throws Exception {
		Category category = new Category("Drama");
		repo.save(category);
	}

	@Test
	public void testFindByName() {
		String name = "Drama";
		
		Category c = repo.findByName(name);
		
		assertNotNull(c);
		assertEquals(name, c.getName());
	}

}
