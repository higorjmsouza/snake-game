package higor.jacinto.snake;

import higor.jacinto.snake.ai.SimpleHeuristicAI;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AIController {

    public static void run() {
        final var gamePanel = new GamePanel();
        gamePanel.setIAJogando(new SimpleHeuristicAI());

        final var gameScene = new Scene(new StackPane(gamePanel), 600, 400);
        final var stage = new Stage();
        stage.setTitle("IA no controle");
        stage.setScene(gameScene);
        stage.show();
    }
}
