package io.github.NumberFactory.model.board;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.CellType;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public class Board {
    public final int width;
    public final int height;
    private final Cell[][] cells;
    private int editingX = -1;
    private int editingY = -1;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(CellType.FLOOR);
            }
        }
    }

    public void clear() {
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++) {
                cells[x][y].clear();
                rewireNeighbors(x, y);
            }
        }
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                recalculateValid(x, y);
            }
        }
        editingX = -1;
        editingY = -1;
    }

    public boolean inBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    public Cell getCell(int x, int y) {
        if (!inBounds(x, y)) {
            return null;
        }
        return cells[x][y];
    }

    public boolean hasEditingCell() {
        return editingX >= 0;
    }
    public int getEditingX() {
        return editingX;
    }
    public int getEditingY() {
        return editingY;
    }

    public boolean placeIntoCell(int x, int y, Component c) {
        if (!inBounds(x, y)) {
            return false;
        }
        if (hasEditingCell() && (editingX != x || editingY != y)) {
            return false;
        }

        Cell cell = cells[x][y];
        if (c == null) {
            cell.clear();
            rewireNeighbors(x, y);
            recalculateValid(x, y);
            if (editingX == x && editingY == y) {
                editingX = -1;
                editingY = -1;
            }
            return true;
        }
        if (!CellType.canPlaceComponent(cell.type)) {
            return false;
        }
        if (!cell.isEmpty()) {
            return false;
        }

        cell.setComponent(c);
        cell.enterEdit();
        rewireNeighbors(x, y);
        recalculateValid(x, y);
        editingX = x;
        editingY = y;
        return true;
    }

    public boolean clearCell(int x, int y) {
        return placeIntoCell(x, y, null);
    }

    public boolean setPort(int x, int y, Directions dir, PortType port) {
        if (!inBounds(x, y)) {
            return false;
        }
        if (editingX != x || editingY != y) {
            return false;
        }

        Cell cell = cells[x][y];
        if (cell.isEmpty()) {
            return false;
        }
        cell.getComponent().setPort(dir, port);
        recalculateValid(x, y);
        return true;
    }

    public boolean cyclePort(int x, int y, Directions dir) {
        if (!inBounds(x, y)) {
            return false;
        }
        if (editingX != x || editingY != y) {
            return false;
        }
        if (!cells[x][y].cyclePort(dir)) {
            return false;
        }

        recalculateValid(x, y);
        return true;
    }

    public boolean tryCommitEdit(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        if (editingX != x || editingY != y) {
            return false;
        }
        if (!cells[x][y].tryCommitEdit()) {
            return false;
        }

        editingX = -1;
        editingY = -1;
        return true;
    }

    public boolean enterEdit(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        if (hasEditingCell() && (editingX != x || editingY != y)) {
            return false;
        }

        Cell cell = cells[x][y];
        if (cell.isEmpty()) {
            return false;
        }

        cell.enterEdit();
        editingX = x;
        editingY = y;
        return true;
    }

    public void markInvalid(int x, int y) {
        if (!inBounds(x, y)) {
            return;
        }
        cells[x][y].setValid(false);
    }

    public void rewireAll() {
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                rewireNeighbors(x, y);
            }
        }
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                recalculateValid(x, y);
            }
        }
    }

    private void rewireNeighbors(int x, int y) {
        Component self = cells[x][y].getComponent();
        for (Directions d : Directions.values()) {
            int nx = x + d.dx;
            int ny = y + d.dy;
            Component neighbor = inBounds(nx, ny) ? cells[nx][ny].getComponent() : null;
            if (self != null) {
                self.connect(d, neighbor);
            }
            if (neighbor != null) {
                neighbor.connect(d.opposite(), self);
            }
        }
    }

    private void recalculateValid(int x, int y) {
        if (!inBounds(x, y)) {
            return;
        }
        Cell cell = cells[x][y];
        if (cell.isEmpty()) {
            cell.setValid(false);
            return;
        }
        cell.setValid(cell.getComponent().isValid());
    }
}
