package higor.jacinto.snake.ai;

import higor.jacinto.snake.Direction;
import higor.jacinto.snake.GameState;

public class SimpleHeuristicAI implements SnakeAI {

    @Override
    public Direction nextMove(final GameState state) {
        final var head = state.snake().getFirst();
        final var food = state.food();

        if (food.x() > head.x()) {
            return Direction.RIGHT;
        }

        if (food.x() < head.x()) {
            return Direction.LEFT;
        }

        if (food.y() > head.y()) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }

    @Override
    public void train(final GameState state, final Direction playerMove) {
        //TODO
    }
}
