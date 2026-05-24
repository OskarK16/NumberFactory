package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public class EditController {

    private final Board board;

    public EditController(Board board) {
        this.board = board;
    }

    public boolean hasActiveEdit() { return board.hasEditingCell(); }
    public int getEditingX()       { return board.getEditingX(); }
    public int getEditingY()       { return board.getEditingY(); }

    public boolean setPort(Directions dir, PortType port) {
        if (!board.hasEditingCell()) return false;
        return board.setPort(board.getEditingX(), board.getEditingY(), dir, port);
    }

    public boolean cyclePort(Directions dir) {
        if (!board.hasEditingCell()) return false;
        return board.cyclePort(board.getEditingX(), board.getEditingY(), dir);
    }

    public boolean cyclePortPrev(Directions dir) {
        if (!board.hasEditingCell()) return false;
        return board.cyclePortPrev(board.getEditingX(), board.getEditingY(), dir);
    }

    public boolean commit() {
        if (!board.hasEditingCell()) return false;
        return board.tryCommitEdit(board.getEditingX(), board.getEditingY());
    }

    public boolean cancel() {
        if (!board.hasEditingCell()) return false;
        return board.clearCell(board.getEditingX(), board.getEditingY());
    }

    public boolean reopen(int x, int y) {
        return board.enterEdit(x, y);
    }
}
