package com.softwareu.movies.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.softwareu.movies.dto.MovieDTO;
import com.softwareu.movies.model.Image;

public interface ImageService {
	
	Image findImageById(Long id);

	byte[] getActualImage(String imagePath) throws IOException;

	String addImage(MovieDTO movieDTO, MultipartFile file, String uploadDir);

	Image getImageObj(MultipartFile file, String uploadDir);

	String getImageUrl(Image image);
}
