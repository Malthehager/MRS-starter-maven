// project structure
package dk.easv.mrs.DAL;
import dk.easv.mrs.BE.Movie;
//java imports
import java.util.List;

public interface IMovieDataAccess {

     List<Movie> getAllMovies() throws Exception;

     Movie createMovie(Movie newMovie) throws Exception;

     void updateMovie(Movie movie) throws Exception;

     void deleteMovie(Movie movie) throws Exception;

}
