package io.github.NumberFactory.utils;

public enum Directions {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    public final int dx;
    public final int dy;

    Directions(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Directions opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}
