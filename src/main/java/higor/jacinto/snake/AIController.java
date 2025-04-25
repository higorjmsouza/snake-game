package higor.jacinto.snake;

import higor.jacinto.snake.ai.SimpleHeuristicAI;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class AIController {

    public static void run(final Stage stage) {
        final var gamePanel = new GamePanel();
        gamePanel.setIAJogando(new SimpleHeuristicAI());

        final var gameScene = new Scene(new StackPane(gamePanel), 600, 400);
        stage.getIcons().add(new Image(Objects.requireNonNull(AIController.class.getResourceAsStream("/images/snake-game.png"))));
        stage.setTitle("IA no controle");
        stage.setScene(gameScene);
        stage.show();

        gamePanel.setOnGameOver(() -> {
            final var score = gamePanel.getSnake().size() - 1;
            Platform.runLater(() -> GameOver.show(stage, score, true));
        });
    }
}
