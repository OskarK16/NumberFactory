package io.github.NumberFactory.utils;

public enum CellType {
    FLOOR,
    WALL;

    public static boolean canPlaceComponent(CellType type) {
        return type.equals(FLOOR);
    }
}
