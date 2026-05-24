package io.github.NumberFactory.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import io.github.NumberFactory.model.SimulationState;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;

public class InputHandler implements InputProcessor {

    private final GameController game;
    private final EditController edit;
    private final HotbarController hotbar;
    private final ScreenToCellMapper mapper;

    private static final int DRAG_THRESHOLD = 6;
    private int rightStartX = -1;
    private int rightStartY = -1;

    public InputHandler(GameController game,
                        EditController edit,
                        HotbarController hotbar,
                        ScreenToCellMapper mapper) {
        this.game = game;
        this.edit = edit;
        this.hotbar = hotbar;
        this.mapper = mapper;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT)  return handleLeftClick(screenX, screenY);
        if (button == Input.Buttons.RIGHT) {
            rightStartX = screenX;
            rightStartY = screenY;
            return false;
        }
        return false;
    }

    private boolean handleLeftClick(int screenX, int screenY) {
        int[] cell = mapper.toCell(screenX, screenY);
        Board board = game.getBoard();


        if (edit.hasActiveEdit()) {
            int ex = edit.getEditingX();
            int ey = edit.getEditingY();
            if (cell != null && cell[0] == ex && cell[1] == ey) {
                Directions dir = mapper.toPortDirection(screenX, screenY);
                if (dir != null) return edit.cyclePort(dir);
                return false;
            }
            return edit.commit();
        }

        if (cell == null) return false;
        int x = cell[0], y = cell[1];
        if (!board.inBounds(x, y)) return false;
        Cell c = board.getCell(x, y);

        if (c.isEmpty()) {
            Component fresh = hotbar.createSelected();
            if (fresh == null) return false;
            return game.placeComponent(x, y, fresh);
        }
        return edit.reopen(x, y);
    }

    private boolean handleRightClick(int screenX, int screenY) {
        int[] cell = mapper.toCell(screenX, screenY);
        if (cell == null) return false;
        int x = cell[0], y = cell[1];
        Board board = game.getBoard();
        if (!board.inBounds(x, y)) return false;
        if (board.getCell(x, y).isEmpty()) return false;
        return game.removeComponent(x, y);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9) {
            int index = keycode - Input.Keys.NUM_0;
            if (index == 0) return false;
            return hotbar.selectFromUtility(index-1);
        }
        if (keycode == Input.Keys.ENTER)  return edit.commit();
        if (keycode == Input.Keys.ESCAPE) return edit.cancel();
        if (keycode == Input.Keys.SPACE)  return handleSpace();
        return false;
    }

    private boolean handleSpace() {
        SimulationState s = game.getSimulationState();
        return switch (s) {
            case IDLE      -> game.start();
            case RUNNING   -> game.pause();
            case PAUSED    -> game.resume();
            case COMPLETED -> game.reset();
        };
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (isCtrlDown()) return false;

        if (edit.hasActiveEdit()) {
            int mx = Gdx.input.getX();
            int my = Gdx.input.getY();
            int[] cell = mapper.toCell(mx, my);
            if (cell != null && cell[0] == edit.getEditingX() && cell[1] == edit.getEditingY()) {
                Directions dir = mapper.toPortDirection(mx, my);
                if (dir != null) return edit.cyclePort(dir);
            }
            return false;
        }

        if (amountY > 0)      hotbar.selectNext();
        else if (amountY < 0) hotbar.selectPrev();
        else return false;
        return true;
    }

    private boolean isCtrlDown() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    @Override
    public boolean touchUp(int sx, int sy, int p, int b) {
        if (b == Input.Buttons.RIGHT && rightStartX >= 0) {
            int dx = sx - rightStartX;
            int dy = sy - rightStartY;
            rightStartX = -1;
            rightStartY = -1;
            if (Math.abs(dx) + Math.abs(dy) < DRAG_THRESHOLD) {
                handleRightClick(sx, sy);
            }
        }
        return false;
    }

    @Override public boolean touchDragged(int sx, int sy, int p)          { return false; }
    @Override public boolean mouseMoved(int sx, int sy)                   { return false; }
    @Override public boolean keyUp(int kc)                                { return false; }
    @Override public boolean keyTyped(char c)                             { return false; }
    @Override public boolean touchCancelled(int sx, int sy, int p, int b) { return false; }
}
