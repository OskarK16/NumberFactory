package io.github.NumberFactory.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import io.github.NumberFactory.input.GameAction;
import io.github.NumberFactory.input.KeyMapper;
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

    public InputHandler(GameController game, EditController edit, HotbarController hotbar, ScreenToCellMapper mapper) {
        this.game = game;
        this.edit = edit;
        this.hotbar = hotbar;
        this.mapper = mapper;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            return false;
        }

        if (button == Input.Buttons.RIGHT) {
            return handleInteract(screenX, screenY);
        }

        if (button == Input.Buttons.MIDDLE) {
            return handleRemove(screenX, screenY);
        }

        return false;
    }

    private boolean handleInteract(int screenX, int screenY) {
        int[] cell = mapper.toCell(screenX, screenY);
        Board board = game.getBoard();

        if (edit.hasActiveEdit()) {
            int ex = edit.getEditingX();
            int ey = edit.getEditingY();
            if (cell != null && cell[0] == ex && cell[1] == ey) {
                Directions dir = mapper.toPortDirection(screenX, screenY);
                if (dir != null) {
                    return edit.cyclePort(dir);
                }
                return false;
            }
            return edit.commit();
        }

        if (cell == null) {
            return false;
        }
        int x = cell[0], y = cell[1];
        if (!board.inBounds(x, y)) {
            return false;
        }
        Cell c = board.getCell(x, y);

        if (c.isEmpty()) {
            Component fresh = hotbar.createSelected();
            if (fresh == null) {
                return false;
            }
            return game.placeComponent(x, y, fresh);
        }

        return edit.reopen(x, y);
    }

    private boolean handleRemove(int screenX, int screenY) {
        edit.commit();
        int[] cell = mapper.toCell(screenX, screenY);
        if (cell == null) {
            return false;
        }
        int x = cell[0], y = cell[1];
        Board board = game.getBoard();
        if (!board.inBounds(x, y)) {
            return false;
        }
        if (board.getCell(x, y).isEmpty()) {
            return false;
        }
        return game.removeComponent(x, y);
    }

    @Override
    public boolean keyDown(int keycode) {
        GameAction action = KeyMapper.getAction(keycode);

        return switch (action) {
            case CYCLE_HOTBAR_CATEGORY -> handleTab();
            case EDIT_COMMIT -> edit.commit();
            case TOGGLE_SIMULATION -> handleSpace();
            case CANCEL_OR_CLOSE -> {
                if (edit.hasActiveEdit()) {
                    yield edit.cancel();
                }
                else if (hotbar.isSubOpen()) {
                    hotbar.closeSub();
                    hotbar.selectFromUtility(0);
                    yield true;
                }
                yield false;
            }
            case HOTBAR_SLOT_1 -> handleHotbarSlot(1);
            case HOTBAR_SLOT_2 -> handleHotbarSlot(2);
            case HOTBAR_SLOT_3 -> handleHotbarSlot(3);
            case HOTBAR_SLOT_4 -> handleHotbarSlot(4);
            case HOTBAR_SLOT_5 -> handleHotbarSlot(5);
            case HOTBAR_SLOT_6 -> handleHotbarSlot(6);
            case HOTBAR_SLOT_7 -> handleHotbarSlot(7);
            case HOTBAR_SLOT_8 -> handleHotbarSlot(8);
            case HOTBAR_SLOT_9 -> handleHotbarSlot(9);
            case ACTION_UNKNOWN -> false;
        };
    }

    private boolean handleHotbarSlot(int num) {
        int index = num - 1;

        HotbarController.SubCategory openSub = hotbar.getOpenSub();
        int utilitySize = hotbar.getUtility().getEntries().size();

        if (openSub == null) {
            if (index < utilitySize) {
                return hotbar.selectFromUtility(index);
            }
            else if (index == utilitySize) {
                hotbar.openSub(HotbarController.SubCategory.ARITHMETIC);
                return true;
            }
            else if (index == utilitySize + 1) {
                hotbar.openSub(HotbarController.SubCategory.LOGIC);
                return true;
            }
        }
        else if (openSub == HotbarController.SubCategory.ARITHMETIC) {
            if (index < hotbar.getArithmetic().getEntries().size()) {
                return hotbar.selectFromArithmetic(index);
            }
        }
        else if (openSub == HotbarController.SubCategory.LOGIC) {
            if (index < hotbar.getLogic().getEntries().size()) {
                return hotbar.selectFromLogic(index);
            }
        }
        return false;
    }

    private boolean handleSpace() {
        SimulationState s = game.getSimulationState();
        return switch (s) {
            case IDLE -> game.start();
            case RUNNING -> game.pause();
            case PAUSED -> game.resume();
            case COMPLETED -> game.reset();
        };
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            return false;
        }

        if (isCtrlDown()) {
            return false;
        }

        if (edit.hasActiveEdit()) {
            int mx = Gdx.input.getX();
            int my = Gdx.input.getY();
            int[] cell = mapper.toCell(mx, my);
            if (cell != null && cell[0] == edit.getEditingX() && cell[1] == edit.getEditingY()) {
                Directions dir = mapper.toPortDirection(mx, my);
                if (dir != null) {
                    return edit.cyclePort(dir);
                }
            }
            return false;
        }

        if (amountY > 0) {
            hotbar.selectNext();
        }
        else if (amountY < 0) {
            hotbar.selectPrev();
        }
        else {
            return false;
        }
        return true;
    }

    private boolean isCtrlDown() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    private boolean handleTab() {
        int utilitySize = hotbar.getUtility().getEntries().size();
        HotbarController.SubCategory openSub = hotbar.getOpenSub();

        if (openSub == null) {
            int currentIndex = hotbar.getUtility().getSelectedIndex();

            if (currentIndex < utilitySize - 1) {
                return hotbar.selectFromUtility(currentIndex + 1);
            }
            else {
                hotbar.openSub(HotbarController.SubCategory.ARITHMETIC);
                return true;
            }
        }
        else if (openSub == HotbarController.SubCategory.ARITHMETIC) {
            hotbar.openSub(HotbarController.SubCategory.LOGIC);
            return true;
        }
        else if (openSub == HotbarController.SubCategory.LOGIC) {
            hotbar.closeSub();
            return hotbar.selectFromUtility(0);
        }

        return false;
    }

    @Override public boolean touchUp(int sx, int sy, int p, int b) { return false; }
    @Override public boolean touchDragged(int sx, int sy, int p) { return false; }
    @Override public boolean mouseMoved(int sx, int sy) { return false; }
    @Override public boolean keyUp(int kc) { return false; }
    @Override public boolean keyTyped(char c) { return false; }
    @Override public boolean touchCancelled(int sx, int sy, int p, int b) { return false; }
}
