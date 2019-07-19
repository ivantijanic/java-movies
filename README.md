# About Project
Backend application for movies and categories management which provide REST API for multiple client usage .

For easily movie and category management this application provide well defined API support requirements for:
* retrieves all movies with or without requested pagination from the client app
*	searching for movie by movie name	adding or deleting a movie from the movie lis	retrieves all categories and subcategories
*	retrieves movies from particular category with or without requested pagination from the client app
*	searching for movie by chosen category
*	adding or deleting a category from the category list

# Technologies
Here are the languages used, the tools and its versions.

v	Java (JDK)8
*	Maven
*	Spring Boot
*	Lombok
*	Redis
*	H2
*	JUnit
*	Mockito

# Note
For creating and adding new movie to the server , include image file along with your json saved in  .json file

# Files
*	/documentation/documentation.txt
*	/uml/models.uml
*	c:/temp/count_of_calls.txt - It is going to be created at first rest call
*	c:/temp/images - It is going to be created at build

# Object Model (model.uml)
*	Each movie have imdb as Id, and fields as movie title, year when it’s released, description and lists of categories each movie belongs to ( movie can be in more then one category).
*	Category beside it’s name also owns a list of movies from that category, and list of subcategory ( category can have more then one subcategory).
*	Each subcategory has a name, and can be attached to many categories.
