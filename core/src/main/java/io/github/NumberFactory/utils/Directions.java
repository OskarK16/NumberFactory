package io.github.NumberFactory.utils;

public enum Directions {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Directions opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}
