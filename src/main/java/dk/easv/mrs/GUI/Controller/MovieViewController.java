// project structure
package dk.easv.mrs.GUI.Controller;
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;
// javafx imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
//java imports
import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {


    public TextField txtMovieSearch;
    //public ListView<Movie> lstMovies;
    private MovieModel movieModel;
    @FXML
    private Button btnClick;
    @FXML
    private TextField txtTitle, txtYear;
    @FXML
    private TableView<Movie> tblMovies;
    @FXML
    private TableColumn<Movie, String> colTitle;
    @FXML
    private TableColumn<Movie, Integer> colYear;
    public MovieViewController()  {

        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // setup columns in tableview
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        // connect tableview to the ObservableList (FilteredList)
        tblMovies.setItems(movieModel.getObservableMovies());

        // table view listener (when user selects a movie in the tableview)
        tblMovies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                txtTitle.setText(newValue.getTitle());
                txtYear.setText(Integer.toString(newValue.getYear()));

            }
            else {
                txtTitle.setText("");
                txtYear.setText("");

            }
        });
        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            movieModel.getObservableMovies().setPredicate(movie -> {

                // If filter text is empty, display all movies.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (movie.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return Integer.toString(movie.getYear()).contains(lowerCaseFilter);
            });
        });

    }

    private void displayError(Throwable t)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    @FXML
    private void btnHandleClick(ActionEvent actionEvent) throws Exception {
        // call model
        // Get user moive data from UI
        String title = txtTitle.getText();
        // Skal lave gettext (String) om til en int
        int year = Integer.parseInt(txtYear.getText());

        // new movie obj.
        Movie newMovie = new Movie(-1, year, title);
        // call the model to create the movie in the dal
        movieModel.createMovie(newMovie);

    }

    @FXML
    private void onActionUpdate(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {

            // update movie based on textfield inputs from user
            selectedMovie.setTitle(txtTitle.getText());
            selectedMovie.setYear(Integer.parseInt(txtYear.getText()));

            // Update movie in DAL layer (through the layers)
            try {
                movieModel.updateMovie(selectedMovie);
            }
            catch (Exception err){
                displayError(err);
            }

            // ask controls to refresh their content
            tblMovies.refresh();
        }
    }

    @FXML
    private void onActionDelete(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null){

            try{
                movieModel.deleteMovie(selectedMovie);
            }
            catch (Exception err){
                displayError(err);
            }
        }
    }
}
