package esprit.lanforlife.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for SignInController


        // Load the FXML file for MainController
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/esprit/lanforlife/demo/Main.fxml"));
        Parent mainRoot = mainLoader.load();

        // Set up the scene with the main root
        Scene scene = new Scene(mainRoot);
        scene.setFill(Color.TRANSPARENT);

        // Set up the stage
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
