package dad.controller.palabras;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
			BufferedReader fr = new BufferedReader(new FileReader("listaPalabras.txt"));
			String words = fr.readLine();
			while (words != null) {
				palabras.add(words);
				words = fr.readLine();
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void onNuevoAction(ActionEvent event) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("Nuevo nombre");
		dialog.setHeaderText("Añadir un nuevo nombre a la lista");
		dialog.setContentText("Nombre: ");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent() && !name.get().isBlank() && !palabras.contains(name.get())) {
			palabras.add(name.get().trim());
		}

		FileWriter salida = new FileWriter("listaPalabras.txt");

		for (int i = 0; i < palabras.getSize(); i++) {
			salida.append(palabras.get(i));
			salida.append("\n");
		}

		salida.close();
	}

	@FXML
	void onQuitarAction(ActionEvent event) throws IOException {
		System.out.println(palabraSeleccionadaProperty().get());

		palabras.remove(palabras.indexOf(palabraSeleccionadaProperty().get()));

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

}
