package io.github.NumberFactory.model.board;

public class Board {
    public final int width;
    public final int height;
    private final Cell[][] cells;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                cells[x][y] = new Cell();
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException("Cell out of bounds: " + x + ", " + y);
        return cells[x][y];
    }
}
