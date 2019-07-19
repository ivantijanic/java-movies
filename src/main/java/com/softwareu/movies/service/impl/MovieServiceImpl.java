package com.softwareu.movies.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import com.softwareu.movies.converter.Movie2MovieDTO;
import com.softwareu.movies.converter.MovieDTO2Movie;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.exception.NotFoundException;
import com.softwareu.movies.helper.ImdbIdUtil;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import com.softwareu.movies.repository.ImageRepository;
import com.softwareu.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.softwareu.movies.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieDTO2Movie toMovie;

    @Autowired
    private Movie2MovieDTO toMovieDTO;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<Movie> findAll(Pageable page) { return movieRepository.findAll(page); }

    @Override
    @Cacheable(value = "movie", key = "#id")
    public MovieDTO findOne(String id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (!movieOptional.isPresent())
            throw new NotFoundException("Movie with id of " + id + " not found!");

        return toMovieDTO.convert(movieOptional.get());
    }

    @Override
    @Cacheable(value = "movie", key = "#title")
    public MovieDTO findByTitle(String title) {
        Optional<Movie> movieOptional = (movieRepository.findByTitle(title));
        if (!movieOptional.isPresent())
            throw new NotFoundException("Movie " + title + " not found!");
        return toMovieDTO.convert(movieOptional.get());
    }

    @Override
    @Transactional
    @CacheEvict(value="movie")
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    @CacheEvict(value="movie")
    public MovieDTO save(MovieDTO movieDTO, Image image) {
    	Movie movie = toMovie.convert(movieDTO);
    	if (image != null) {
    		imageRepository.save(image);
    		movie.addImage(image);
    	}
 
    	setImdbId(movieDTO);   	
        Movie persisted = movieRepository.save(movie);
        return toMovieDTO.convert(persisted);
    }

	@Override
    @Transactional
    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }

	@Override
	@Transactional
	public void delete(String id) {
		movieRepository.deleteById(id);
	}
	
	 //in case that IMDB ID hasn't been provided, generate a new random one.
	 private void setImdbId(MovieDTO movieDTO) {
			if (movieDTO.getImdbId() != null)
				return;
			
			movieDTO.setImdbId(ImdbIdUtil.randomImdbId());
			while (checkIfIdExists(movieDTO.getImdbId())) {
				movieDTO.setImdbId(ImdbIdUtil.randomImdbId());
			}
	 }
	 
	 private boolean checkIfIdExists(String id) {
		 Optional<Movie> optionalMovie = movieRepository.findById(id);
		 if (optionalMovie.isPresent())
			 return true;
		 return false;
	 }

	@Override
	public Page<MovieDTO> findByCategoryName(String name, Pageable page) {
		Page<Movie> movies = movieRepository.findByCategoryName(name, page);
        Page<MovieDTO> converted = new PageImpl<>(toMovieDTO.convert(movies.getContent()));
        return converted;
	}
}
