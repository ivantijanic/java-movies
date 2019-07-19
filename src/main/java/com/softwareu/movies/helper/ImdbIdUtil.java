package com.softwareu.movies.helper;

import java.util.Random;

public class ImdbIdUtil {
	
	private static Random random = new Random();
	
	public static String randomImdbId() {
		StringBuilder sb = new StringBuilder("tt0");
		int randomInt = random.ints(100000, 999999).findFirst().getAsInt();
		sb.append(randomInt);
		return sb.toString();
	}
	
}
