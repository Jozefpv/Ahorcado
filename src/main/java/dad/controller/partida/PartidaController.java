package dad.controller.partida;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import dad.ahorcado.AhorcadoApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class PartidaController implements Initializable {
	//
	ArrayList<String> datos = new ArrayList<>();
	ArrayList<String> puntuaciones = new ArrayList<>();

	String palabraOculta = "", palabraResolver = "";
	BufferedReader fr;
	Random rd = new Random();
	int cont, puntuacion = 0;
	int numRandom;

	// model
	private StringProperty palabra = new SimpleStringProperty();
	private StringProperty letra = new SimpleStringProperty();

	public final StringProperty palabraProperty() {
		return this.palabra;
	}

	public final String getPalabra() {
		return this.palabraProperty().get();
	}

	public final void setPalabra(final String palabra) {
		this.palabraProperty().set(palabra);
	}

	public final StringProperty letraProperty() {
		return this.letra;
	}

	public final String getLetra() {
		return this.letraProperty().get();
	}

	public final void setLetra(final String letra) {
		this.letraProperty().set(letra);
	}

	// view
	@FXML
	private ImageView imageContent;

	@FXML
	private Button letraButton, resolverButton;

	@FXML
	private TextField letraText;

	@FXML
	private Label letrasUtilizadasLabel, palabraLabel, puntosLabel;

	@FXML
	private BorderPane view;

	public PartidaController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// load word
		try {
			fr = new BufferedReader(new FileReader("listaPalabras.txt"));
			String words = fr.readLine();
			while (words != null) {
				datos.add(words);
				words = fr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadWord();

		try {
			BufferedReader fr = new BufferedReader(new FileReader("puntuaciones.txt"));
			String puntos = fr.readLine();
			while (puntos != null) {
				puntuaciones.add(puntos);
				puntos = fr.readLine();
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadWord() {
		cont = 0;
		setImage();
		letrasUtilizadasLabel.setText("");
		palabraOculta = "";
		numRandom = rd.nextInt(datos.size());
		setPalabra(datos.get(numRandom).toUpperCase());
		for (int i = 0; i < getPalabra().length(); i++) {
			palabraOculta += "_";
		}
		System.out.println(palabra.get());
		palabraLabel.setText(palabraOculta);
	}

	private void setImage() {
		cont++;
		if (cont < 10) {
			imageContent.setImage(new Image(String.format("/images/%d.png", cont)));
		} else {
			partidaFinalizada();
		}

	}

	private void partidaFinalizada() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Fin de la partida");
		alert.setHeaderText("Has perdido");
		alert.setContentText("La respuesta correcta era: " + palabra.get() + " - Pts: " + puntuacion);
		alert.showAndWait();
		
		try {
			setPuntuacion();
			AhorcadoApp.primaryStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setPuntuacion() throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(AhorcadoApp.primaryStage);
		dialog.setTitle("Jugador");
		dialog.setHeaderText("Añadir el nombre del jugador");
		dialog.setContentText("Nombre: ");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent() && !name.get().isBlank()) {
			puntuaciones.add(name.get().trim().toUpperCase() + " - " + puntuacion);
		}
		
		
		FileWriter salida = new FileWriter("puntuaciones.txt");
		for (int i = 0; i < puntuaciones.size(); i++) {
			salida.append(puntuaciones.get(i));
			salida.append("\n");

		}
		salida.close();
	}

	@FXML
	void onLetraAction(ActionEvent event) {
		if (!letraText.getText().isEmpty()) {
			setLetra((letraText.getText().charAt(0) + "").toUpperCase());
			letrasUtilizadasLabel.setText(letrasUtilizadasLabel.getText() + " " + getLetra());
			boolean check = false;
			for (int i = 0; i < palabra.get().length(); i++) {
				if (palabra.get().charAt(i) == getLetra().charAt(0)) {
					palabraResolver += getLetra().charAt(0);
					puntuacion++;
					check = true;
				} else if (palabraOculta.charAt(i) != '_') {
					palabraResolver += palabraOculta.charAt(i);
				} else {
					palabraResolver += "_";
				}

			}
			if (!check) {
				setImage();
			}
			palabraOculta = palabraResolver;
			palabraLabel.setText(palabraOculta);
			palabraResolver = "";
			letraText.setText("");
			puntosLabel.setText(puntuacion + "");

			if (palabraOculta.equals(palabra.get())) {
				loadWord();
			}
		}

	}

	@FXML
	void onResolverAction(ActionEvent event) {
		if (letraText.getText().toUpperCase().equals(palabra.get())) {
			puntuacion += palabra.get().length();
			puntosLabel.setText(puntuacion + "");
			loadWord();
			letraText.setText("");
		} else {
			partidaFinalizada();
		}
	}

	public BorderPane getView() {
		return view;
	}

}
