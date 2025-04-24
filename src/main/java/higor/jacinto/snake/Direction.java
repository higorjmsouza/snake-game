package higor.jacinto.snake;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ;
    
    public static boolean direcaoValida(final Direction atual, final Direction nova) {
        return switch (atual) {
            case UP -> nova != Direction.DOWN;
            case DOWN -> nova != Direction.UP;
            case LEFT -> nova != Direction.RIGHT;
            case RIGHT -> nova != Direction.LEFT;
        };
    }
}
