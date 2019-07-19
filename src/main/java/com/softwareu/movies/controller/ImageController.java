package com.softwareu.movies.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.service.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.softwareu.movies.service.MovieService;
import com.softwareu.movies.service.StorageService;

@Controller
@RequestMapping("api/images")
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private MovieService movieService;
	
	@Autowired
	private StorageService storageService;

	@Value("${image.save.path}")
	private String UPLOAD_DIR;
	
	@GetMapping("/{id}")
	public void renderImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
		Image imageObj = imageService.findImageById(id);
		byte[] image = imageService.getActualImage(imageObj.getImagePath());
		response.setContentType("image/jpeg");
		InputStream input = new ByteArrayInputStream(image);
		IOUtils.copy(input, response.getOutputStream());
	}
	
	@PostMapping("/{movieId}")
	public ResponseEntity<String> addImage(@PathVariable String movieId, @RequestParam MultipartFile file) {
		MovieDTO movie = movieService.findOne(movieId);
		storageService.store(file);
		String imageUrl = imageService.addImage(movie, file, UPLOAD_DIR);
		return new ResponseEntity<String>(imageUrl, HttpStatus.CREATED);
	}
}
