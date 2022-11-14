package dad.controller.puntuacion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class PuntuacionController implements Initializable {

	// model
	private ListProperty<String> puntuaciones = new SimpleListProperty<>(FXCollections.observableArrayList());

	public final ListProperty<String> puntuacionesProperty() {
		return this.puntuaciones;
	}

	public final ObservableList<String> getPuntuaciones() {
		return this.puntuacionesProperty().get();
	}

	public final void setPuntuaciones(final ObservableList<String> puntuaciones) {
		this.puntuacionesProperty().set(puntuaciones);
	}

	// view

	@FXML
	private ListView<String> listaPuntuaciones;

	@FXML
	private BorderPane view;
	
	public PuntuacionController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PuntuacionView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// bindings
		listaPuntuaciones.itemsProperty().bind(puntuaciones);

		// load data
		try {
			puntuaciones.addAll(cargarFichero());
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		} 
	}
	
	private List<String> cargarFichero() throws URISyntaxException, IOException {
		Path scoreFile = Paths.get("puntos.txt");
		List<String> score = Files.readAllLines(scoreFile, StandardCharsets.UTF_8);
		return score;
	}
	
	public BorderPane getView() {
		return view;
	}

}
