package io.github.NumberFactory.model;

import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.components.Component;

public class Machine {
    private final Board board;

    public Machine(Board board) {
        this.board = board;
    }

    public void tick() {
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Component c = board.getCell(x, y).getComponent();
                if (c != null) c.tick();
            }
        }
    }
}
