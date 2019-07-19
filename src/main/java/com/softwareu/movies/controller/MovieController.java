package com.softwareu.movies.controller;


import java.util.List;

import javax.validation.Valid;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softwareu.movies.converter.Movie2MovieDTO;
import com.softwareu.movies.service.MovieService;
import com.softwareu.movies.service.StorageService;
import com.softwareu.movies.storage.StorageFileNotFoundException;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

	@Value("${image.save.path}")
	private String UPLOAD_DIR;

	@Autowired
	private MovieService movieService;

	@Autowired
	private Movie2MovieDTO toMovieDTO;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ImageService imageService;

	private static final String APPLICATION_JSON = "application/json";

	@GetMapping
	public ResponseEntity<List<MovieDTO>> listAll(@RequestParam(defaultValue = "0") int page,
												  @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return new ResponseEntity<List<MovieDTO>>(
				toMovieDTO.convert(movieService.findAll(pageable).getContent()), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MovieDTO> findOne(@PathVariable String id) {
		return new ResponseEntity<>(movieService.findOne(id), HttpStatus.OK);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<MovieDTO> findByMovieName(@PathVariable String name) {
		return new ResponseEntity<>(movieService.findByTitle(name), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<MovieDTO> create(@RequestPart(value = "movie") @Valid MovieDTO newMovie,
										   @RequestPart(value = "file", required = false) MultipartFile file) {
		
		storageService.store(file);
		Image image = imageService.getImageObj(file, UPLOAD_DIR);
		MovieDTO saved = movieService.save(newMovie, image);
		
		return new ResponseEntity<MovieDTO>(saved, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}", consumes = APPLICATION_JSON)
	public ResponseEntity<MovieDTO> update(@PathVariable String id, @RequestBody @Valid MovieDTO movieDTO) {

		if (!movieDTO.getImdbId().equals(id))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		MovieDTO movie = movieService.findOne(id); //check if the movie exists
		MovieDTO updated = movieService.save(movieDTO, null);
		return new ResponseEntity<MovieDTO>(updated, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		MovieDTO movie = movieService.findOne(id);
		movieService.delete(movie.getImdbId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}