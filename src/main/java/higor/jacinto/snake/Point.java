package higor.jacinto.snake;

public record Point(int x, int y) {

    public Point move(final Direction d) {
        return switch (d) {
            case UP -> new Point(x, y - 1);
            case DOWN -> new Point(x, y + 1);
            case LEFT -> new Point(x - 1, y);
            case RIGHT -> new Point(x + 1, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Point(int x1, int y1))) {
            return false;
        }
        return x == x1 && y == y1;
    }

}
