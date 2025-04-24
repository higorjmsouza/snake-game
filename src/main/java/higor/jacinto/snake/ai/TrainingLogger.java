package higor.jacinto.snake.ai;

import higor.jacinto.snake.Direction;
import higor.jacinto.snake.GameState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TrainingLogger {

    private boolean closed = false;

    private static final String FILE_PATH = System.getProperty("user.home") + "/snake-game-logs/treinamento.log";
    private BufferedWriter writer;

    public TrainingLogger() {
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.home"), "snake-game-logs"));
            writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(final GameState state, final Direction playerMove) {
        if (closed || Objects.isNull(writer)) {
            return;
        }

        try {
            final var head = state.snake().getFirst().toString();
            final var food = state.food().toString();
            final var direction = state.direction().name();
            final var move = playerMove.name();

            writer.write("HEAD=" + head + ";FOOD=" + food + ";DIR=" + direction + ";MOVE=" + move);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (closed) {
            return;
        }

        try {
            if (writer != null) {
                writer.close();
                closed = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
