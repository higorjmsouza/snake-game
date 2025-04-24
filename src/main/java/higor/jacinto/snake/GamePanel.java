package higor.jacinto.snake;

import higor.jacinto.snake.ai.SnakeAI;
import higor.jacinto.snake.ai.TrainingLogger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends Canvas {

    private final int width = 30;  // colunas
    private final int height = 20; // linhas
    private final int tileSize = 20;

    @Getter
    private LinkedList<Point> snake;

    @Getter
    @Setter
    private Direction direction = Direction.RIGHT;
    @Getter
    private Point food;
    @Getter
    private boolean running = false;

    private final Random random = new Random();
    private final TrainingLogger logger;
    private SnakeAI ai;
    private boolean iaJogando = false;
    private boolean esperandoMovimentoIA = false;
    private long lastUpdate = 0;

    public GamePanel(TrainingLogger logger) {
        super(600, 400);
        this.logger = logger;
        init();
    }

    public GamePanel() {
        this(null);
    }

    private void init() {
        resetGame();

        new AnimationTimer() {
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
        }.start();
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
        do {
            final var x = random.nextInt(width);
            final var y = random.nextInt(height);
            p = new Point(x, y);
        } while (snake.contains(p));

        food = p;
    }

    private void update() {
        solicitarMovimentoIA();

        final Point head = snake.getFirst();
        final Point newHead = head.move(direction);

        if (colisaoComParede(newHead)) {
            return;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            placeFood();
        } else {
            snake.removeLast();
        }

        if (colisaoComCorpo(newHead)) {
            return;
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

    private boolean colisaoComParede(Point p) {
        if (p.x < 0 || p.y < 0 || p.x >= width || p.y >= height) {
            encerrarJogo("Colisão com a parede!");
            return true;
        }
        return false;
    }

    private boolean colisaoComCorpo(Point head) {
        if (snake.subList(1, snake.size()).contains(head)) {
            encerrarJogo("Colidiu com o corpo!");
            return true;
        }
        return false;
    }

    private void encerrarJogo(String motivo) {
        running = false;
        if (logger != null) {
            logger.close();
        }
        System.out.println(motivo);
    }

    private void render() {
        var gc = getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // Comida
        gc.setFill(Color.RED);
        gc.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        // Cobra
        gc.setFill(Color.LIME);
        for (final var p : snake) {
            gc.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
        }

        // Debug
        // System.out.println("Snake: " + snake);
        // System.out.println("Food: " + food);
        // System.out.println("Direção: " + direction);
    }

    public void setIAJogando(SnakeAI ai) {
        this.ai = ai;
        this.iaJogando = true;
    }
}