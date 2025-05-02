package higor.jacinto.snake;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOver {

    public static void show(final Stage stage, final Integer score, final boolean iaJogando) {
        final var root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1e1e;");

        final var title = new Text("Game Over");
        title.setFont(Font.font("Verdana", 48));
        title.setFill(Color.RED);

        final var scoreText = new Text("Pontuação Final: " + score);
        scoreText.setFont(Font.font("Verdana", 28));
        scoreText.setFill(Color.WHITE);

        final var restart = getRestart(stage, iaJogando);
        final var backToMenu = getBackToMenu(stage);

        root.getChildren().addAll(title, scoreText, restart, backToMenu);

        final var scene = new Scene(root, 600, 400);
        stage.setScene(scene);
    }

    private static Button getRestart(Stage stage, boolean iaJogando) {
        final var restart = new Button("Reiniciar");
        restart.setStyle("""
                -fx-font-size: 18px;
                -fx-background-color: #2196F3;
                -fx-text-fill: white;
                -fx-padding: 10 20 10 20;
                -fx-background-radius: 10;
                """);

        restart.setOnAction(e -> {
            if (iaJogando) {
                AIController.run(stage);
            } else {
                PlayerController.run(stage);
            }
        });
        return restart;
    }

    private static Button getBackToMenu(Stage stage) {
        final var backToMenu = new Button("Voltar ao Menu");
        backToMenu.setStyle("""
                -fx-font-size: 18px;
                -fx-background-color: #4CAF50;
                -fx-text-fill: white;
                -fx-padding: 10 20 10 20;
                -fx-background-radius: 10;
                """);
        backToMenu.setOnAction(e -> new SnakeGame().start(stage));
        return backToMenu;
    }
}
