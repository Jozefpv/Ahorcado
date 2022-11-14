package dad.ahorcado;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AhorcadoApp extends Application {
	
	public static Stage primaryStage;

	private RootController rootController = new RootController();
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		AhorcadoApp.primaryStage = primaryStage;
		
		primaryStage.setTitle("Ahorcado");
		primaryStage.getIcons().add(new Image("/images/9.png"));
		primaryStage.setScene(new Scene(rootController.getView()));
		primaryStage.show();
	}
	
	public static void main (String[] args) {
		launch(args);
	}

}
