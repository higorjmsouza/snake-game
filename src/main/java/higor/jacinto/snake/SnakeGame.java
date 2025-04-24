package higor.jacinto.snake;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SnakeGame extends Application {

    public void start(Stage stage) {
        stage.setTitle("Snake Game com IA");

        final var jogarBtn = new Button("Jogar");
        final var iaJogarBtn = new Button("IA Jogar");

        jogarBtn.setOnAction(e -> PlayerController.run());
        iaJogarBtn.setOnAction(e -> AIController.run());

        final var layout = new VBox(10, jogarBtn, iaJogarBtn);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        final var scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
