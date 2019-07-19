package com.softwareu.movies.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import com.softwareu.movies.converter.Movie2MovieDTO;
import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.exception.NotFoundException;
import com.softwareu.movies.model.Image;
import com.softwareu.movies.model.Movie;
import com.softwareu.movies.repository.ImageRepository;
import com.softwareu.movies.repository.MovieRepository;
import com.softwareu.movies.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private Movie2MovieDTO toMovieDTO;

	@Override
	public Image findImageById(Long imageId) {
		Optional<Image> optionalImage = imageRepository.findById(imageId);
		if (!optionalImage.isPresent())
			throw new NotFoundException();
		return optionalImage.get();
	}
	
	//get image object with image id and file path from DB  
	@Override
	public Image getImageObj(MultipartFile file, String uploadDir) {
		Image image = null;
		if (file != null) {
			String newPath = file.getOriginalFilename();
			image = new Image(uploadDir + newPath);
		}
		return image;
	}
	
	
	//read the image file as byte array
	@Override
	public byte[] getActualImage(String imagePath) throws IOException {
		File img = new File(imagePath);
		return Files.readAllBytes(img.toPath());
	}


	@Override
	public String addImage(MovieDTO movieDTO, MultipartFile file, String uploadDir) {
		Movie movie = movieRepository.findById(movieDTO.getImdbId()).get();
		Image image = getImageObj(file, uploadDir);
		imageRepository.save(image);
		movie.addImage(image);
		movieRepository.save(movie);
		return getImageUrl(image);
	}
	
	@Override
	public String getImageUrl(Image image) {
//		StringBuilder sb = new StringBuilder( request.getContextPath());
		StringBuilder sb = new StringBuilder("/api/images/");
//		String movieId = image.getMovie().getImdbId();
		Long imageId = image.getId();
//		sb.append(movieId);
//		sb.append("/images/");
		sb.append(imageId);
		return sb.toString();
	}

}
