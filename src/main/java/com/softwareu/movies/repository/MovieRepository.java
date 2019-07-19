package com.softwareu.movies.repository;

import java.util.Optional;

import com.softwareu.movies.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

   Optional<Movie> findByTitle(String title);
   
   @Query("SELECT m FROM Movie m JOIN m.categories c WHERE c.name = :name")
   Page<Movie> findByCategoryName(@Param("name") String name, Pageable pageable);
}
