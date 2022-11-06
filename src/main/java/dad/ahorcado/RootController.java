package dad.ahorcado;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.controller.palabras.PalabrasController;
import dad.controller.partida.PartidaController;
import dad.controller.puntuacion.PuntuacionController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class RootController implements Initializable {

	// controllers
	private PalabrasController palabrasController = new PalabrasController();
	private PartidaController partidaController = new PartidaController();
	private PuntuacionController puntuacionController = new PuntuacionController();


	public final ListProperty<String> palabrasProperty() {
		return this.palabras;
	}

	public final ObservableList<String> getPalabras() {
		return this.palabrasProperty().get();
	}

	public final void setPalabras(final ObservableList<String> palabras) {
		this.palabrasProperty().set(palabras);
	}
	// model

	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());

	// view
	@FXML
	private Tab palabrasTab;

	@FXML
	private Tab partidaTab;

	@FXML
	private Tab puntuacionesTab;

	@FXML
	private TabPane view;

	public RootController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		partidaTab.setContent(partidaController.getView());
		palabrasTab.setContent(palabrasController.getView());
		puntuacionesTab.setContent(puntuacionController.getView());


		// bindings

	}

	public TabPane getView() {
		return view;
	}

}
