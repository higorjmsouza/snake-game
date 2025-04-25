package higor.jacinto.snake;

import higor.jacinto.snake.ai.TrainingLogger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerController {

    public static void run() {
        final var logger = new TrainingLogger();
        final var gamePanel = new GamePanel(logger);
        final var gameScene = new Scene(new javafx.scene.layout.StackPane(gamePanel), 600, 400);
        final var ultimaDirecao = new AtomicReference<>();

        gameScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> {
                    if (Direction.direcaoValida(gamePanel.getDirection(), Direction.UP) && gamePanel.podeAtualizarDirecao()) {
                        gamePanel.setDirection(Direction.UP);
                        gamePanel.marcarDirecaoAtualizada();

                        if (!Objects.equals(ultimaDirecao.get(), Direction.UP)) {
                            logger.log(new GameState(gamePanel.getSnake(), gamePanel.getFood(), gamePanel.getDirection()), Direction.UP);
                            ultimaDirecao.set(Direction.UP);
                        }
                    }
                }

                case DOWN -> {
                    if (Direction.direcaoValida(gamePanel.getDirection(), Direction.DOWN) && gamePanel.podeAtualizarDirecao()) {
                        gamePanel.setDirection(Direction.DOWN);
                        gamePanel.marcarDirecaoAtualizada();

                        if (!Objects.equals(ultimaDirecao.get(), Direction.DOWN)) {
                            logger.log(new GameState(gamePanel.getSnake(), gamePanel.getFood(), gamePanel.getDirection()), Direction.DOWN);
                            ultimaDirecao.set(Direction.DOWN);
                        }
                    }
                }

                case LEFT -> {
                    if (Direction.direcaoValida(gamePanel.getDirection(), Direction.LEFT) && gamePanel.podeAtualizarDirecao()) {
                        gamePanel.setDirection(Direction.LEFT);
                        gamePanel.marcarDirecaoAtualizada();

                        if (!Objects.equals(ultimaDirecao.get(), Direction.LEFT)) {
                            logger.log(new GameState(gamePanel.getSnake(), gamePanel.getFood(), gamePanel.getDirection()), Direction.LEFT);
                            ultimaDirecao.set(Direction.LEFT);
                        }
                    }
                }

                case RIGHT -> {
                    if (Direction.direcaoValida(gamePanel.getDirection(), Direction.RIGHT) && gamePanel.podeAtualizarDirecao()) {
                        gamePanel.setDirection(Direction.RIGHT);
                        gamePanel.marcarDirecaoAtualizada();

                        if (!Objects.equals(ultimaDirecao.get(), Direction.RIGHT)) {
                            logger.log(new GameState(gamePanel.getSnake(), gamePanel.getFood(), gamePanel.getDirection()), Direction.RIGHT);
                            ultimaDirecao.set(Direction.RIGHT);
                        }
                    }
                }
            }
        });

        final var stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(PlayerController.class.getResourceAsStream("/images/snake-game.png"))));
        stage.setTitle("Jogador no controle");
        stage.setScene(gameScene);
        stage.show();

        gamePanel.setOnGameOver(() -> {
            final var score = gamePanel.getSnake().size() - 1;
            Platform.runLater(() -> GameOver.show(stage, score));
        });
    }
}
