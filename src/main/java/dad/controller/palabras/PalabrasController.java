package dad.controller.palabras;


import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.ahorcado.AhorcadoApp;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class PalabrasController implements Initializable {

	// model
	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private StringProperty palabraSeleccionada = new SimpleStringProperty();

	// view
	@FXML
	private Button nuevoButton;

	@FXML
	private ListView<String> palabrasList;

	@FXML
	private Button quitarButton;

	@FXML
	private BorderPane view;

	public PalabrasController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PalabrasView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// bindings
		palabrasList.itemsProperty().bind(palabras);
		palabraSeleccionadaProperty().bind(palabrasList.getSelectionModel().selectedItemProperty());
		quitarButton.disableProperty().bind(palabraSeleccionadaProperty().isNull());

		// load data
		try {
			palabras.addAll(cargarFichero());
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		} 
		

	}
	
	public List<String> cargarFichero() throws URISyntaxException, IOException {
		Path wordsFile = Paths.get("listaPalabras.txt");
		List<String> words = Files.readAllLines(wordsFile, StandardCharsets.UTF_8);
		return words;
	}

	@FXML
	void onNuevoAction(ActionEvent event) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("Nuevo palabra");
		dialog.setHeaderText("Añadir una nueva palabra a la lista");
		dialog.setContentText("Palabra: ");
		Optional<String> word = dialog.showAndWait();
		if (word.isPresent() && !word.get().isBlank() && !palabras.contains(word.get())) {
			palabras.add(word.get().trim());
			guardarPalabras();			
		}

	}

	@FXML
	void onQuitarAction(ActionEvent event) throws IOException {
		System.out.println(palabraSeleccionadaProperty().get());
		palabras.remove(palabras.indexOf(palabraSeleccionadaProperty().get()));
		guardarPalabras();
	}

	private void guardarPalabras() throws IOException {
		FileWriter salida = new FileWriter("listaPalabras.txt");
		for (int i = 0; i < palabras.getSize(); i++) {
			salida.append(palabras.get(i));
			salida.append("\n");
		}
		salida.close();
	}

	public BorderPane getView() {
		return view;
	}

	public final ListProperty<String> palabrasProperty() {
		return this.palabras;
	}

	public final ObservableList<String> getPalabras() {
		return this.palabrasProperty().get();
	}

	public final void setPalabras(final ObservableList<String> palabras) {
		this.palabrasProperty().set(palabras);
	}

	public final StringProperty palabraSeleccionadaProperty() {
		return this.palabraSeleccionada;
	}

	public final String getPalabraSeleccionada() {
		return this.palabraSeleccionadaProperty().get();
	}

	public final void setPalabraSeleccionada(final String palabraSeleccionada) {
		this.palabraSeleccionadaProperty().set(palabraSeleccionada);
	}
	
}
