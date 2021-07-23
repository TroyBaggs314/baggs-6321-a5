package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Inventory extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inventory Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
