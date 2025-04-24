package higor.jacinto.snake;

import java.util.LinkedList;

public record GameState(LinkedList<Point> snake, Point food, Direction direction) {
}
