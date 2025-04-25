package higor.jacinto.snake;

import higor.jacinto.snake.ai.SnakeAI;
import higor.jacinto.snake.ai.TrainingLogger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends Canvas {

    private final int width = 30;  // colunas
    private final int height = 20; // linhas
    private final int tileSize = 20; // tamanho do quadrado

    @Getter
    private LinkedList<Point> snake;

    @Getter
    @Setter
    private Direction direction = Direction.RIGHT;

    @Getter
    private Point food;

    @Getter
    private boolean running = false;

    private final TrainingLogger logger;
    private SnakeAI ai;
    private boolean iaJogando = false;
    private boolean esperandoMovimentoIA = false;

    private long lastUpdate = 0;
    private boolean direcaoAtualizada = false;
    private final Image foodImage;
    private final Image snakeHeadImage;
    private final Image snakeBodyImage;
    private final Image snakeTailImage;

    @Setter
    private Runnable onGameOver;
    private AnimationTimer timer;

    public GamePanel(final TrainingLogger logger) {
        super(600, 400);
        this.logger = logger;
        this.foodImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/apple.png")));
        this.snakeHeadImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/head.png")));
        this.snakeBodyImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/body.png")));
        this.snakeTailImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tail.png")));
        init();
    }

    public GamePanel() {
        this(null);
    }

    private void init() {
        resetGame();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 150_000_000) {
                    if (running) {
                        update();
                        render();
                    }
                    lastUpdate = now;
                }
            }
        };

        timer.start();
    }

    public void parar() {
        if (Objects.nonNull(timer)) {
            timer.stop();
        }
        running = false;
    }

    private void resetGame() {
        snake = new LinkedList<>();
        snake.add(new Point(5, 5));
        direction = Direction.RIGHT;
        placeFood();
        running = true;
    }

    private void placeFood() {
        Point p;
        final var random = new Random();
        do {
            final var x = random.nextInt(width);
            final var y = random.nextInt(height);
            p = new Point(x, y);
        } while (snake.contains(p));

        food = p;
    }

    private void update() {
        solicitarMovimentoIA();
        permitirNovaDirecao();

        final var head = snake.getFirst();
        final var newHead = head.move(direction);

        if (colisaoComParede(newHead) || colisaoComCorpo(newHead)) {
            return;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            placeFood();
        } else {
            snake.removeLast();
        }
    }

    private void solicitarMovimentoIA() {
        if (iaJogando && Objects.nonNull(ai) && !esperandoMovimentoIA) {
            esperandoMovimentoIA = true;
            new Thread(() -> {
                final var novaDirecao = ai.nextMove(new GameState(snake, food, direction));
                Platform.runLater(() -> {
                    if (Direction.direcaoValida(direction, novaDirecao)) {
                        direction = novaDirecao;
                        esperandoMovimentoIA = false;
                    }
                });
            }).start();
        }
    }

    private boolean colisaoComParede(final Point p) {
        if (p.x() < 0 || p.y() < 0 || p.x() >= width || p.y() >= height) {
            encerrarJogo("Colisão com a parede!");
            return true;
        }
        return false;
    }

    private boolean colisaoComCorpo(final Point head) {
        if (snake.subList(1, snake.size()).contains(head)) {
            encerrarJogo("Colidiu com o corpo!");
            return true;
        }
        return false;
    }

    private void encerrarJogo(final String motivo) {
        running = false;
        System.out.println(motivo);
        if (Objects.nonNull(logger)) {
            logger.close();
        }

        if (Objects.nonNull(onGameOver)) {
            onGameOver.run();
        }
    }

    private void render() {
        final var gc = getGraphicsContext2D();

        // Fundo escuro
        gc.setFill(Color.rgb(40, 40, 40));
        gc.fillRect(0, 0, getWidth(), getHeight());

        // Grade
        gc.setStroke(Color.rgb(60, 60, 60, 0.3));
        for (var x = 0; x < getWidth(); x += tileSize) {
            for (var y = 0; y < getHeight(); y += tileSize) {
                gc.strokeRect(x, y, tileSize, tileSize);
            }
        }

        // Comida com imagem
        gc.drawImage(foodImage, food.x() * tileSize, food.y() * tileSize, tileSize, tileSize);

        // Cabeça e corpo com imagem
        for (final var p : snake) {
            if (snake.getFirst().equals(p)) {
                gc.drawImage(snakeHeadImage, p.x() * tileSize, p.y() * tileSize, tileSize, tileSize);
            }

            if (!snake.getFirst().equals(p) && !snake.getLast().equals(p)) {
                gc.drawImage(snakeBodyImage, p.x() * tileSize, p.y() * tileSize, tileSize, tileSize);
            }

            if (snake.size() > 1 && snake.getLast().equals(p)) {
                gc.drawImage(snakeTailImage, p.x() * tileSize, p.y() * tileSize, tileSize, tileSize);
            }
        }

        // Pontuação
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Verdana", 18));
        gc.fillText("Pontuação: " + (snake.size() - 1), 10, 20);
    }

    public void setIAJogando(final SnakeAI ai) {
        this.ai = ai;
        this.iaJogando = true;
    }

    public void permitirNovaDirecao() {
        direcaoAtualizada = false;
    }

    public boolean podeAtualizarDirecao() {
        return !direcaoAtualizada;
    }

    public void marcarDirecaoAtualizada() {
        direcaoAtualizada = true;
    }
}
