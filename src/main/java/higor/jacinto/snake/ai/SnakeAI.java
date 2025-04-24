package higor.jacinto.snake.ai;

import higor.jacinto.snake.Direction;
import higor.jacinto.snake.GameState;

public interface SnakeAI {
    
    Direction nextMove(GameState state);

    void train(GameState state, Direction playerMove);
}
