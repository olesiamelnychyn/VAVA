package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
//		System.out.println("here");
		Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
//		System.out.println(root);
	
        primaryStage.setTitle("Main");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
