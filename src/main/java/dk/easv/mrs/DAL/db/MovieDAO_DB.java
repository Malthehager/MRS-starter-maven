// project structure
package dk.easv.mrs.DAL.db;
import dk.easv.mrs.BE.Movie;
//java imports
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO_DB {
    private MyDatabaseConnector databaseConnector;

    public MovieDAO_DB() throws IOException {
            databaseConnector = new MyDatabaseConnector();
    }

    public List<Movie> getAllMovies() throws SQLException {
        ArrayList<Movie> allMovies = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection())
        {
            String sql = "SELECT * FROM Movie;";

            Statement statement = connection.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    int year = resultSet.getInt("year");
                    String title = resultSet.getString("title");

                    Movie movie = new Movie(id, year, title);
                    allMovies.add(movie);

                }
            }

        }
        return allMovies;
    }


}
