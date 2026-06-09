package io.github.NumberFactory.view.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import io.github.NumberFactory.controller.EditController;
import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.controller.ScreenToCellMapper;
import io.github.NumberFactory.model.SimulationState;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;

public class BoardHoverTracker {

    private final Stage stage;
    private final ScreenToCellMapper mapper;
    private final GameController game;
    private final EditController edit;
    private final HotbarController hotbar;

    private int hoverCellX = -1;
    private int hoverCellY = -1;
    private boolean hoverInBoard = false;
    private final Vector2 stageHitScratch = new Vector2();

    public BoardHoverTracker(Stage stage, ScreenToCellMapper mapper, GameController game, EditController edit, HotbarController hotbar) {
        this.stage = stage;
        this.mapper = mapper;
        this.game = game;
        this.edit = edit;
        this.hotbar = hotbar;
    }

    public void updateHover(int screenX, int screenY) {
        stageHitScratch.set(screenX, screenY);
        stage.screenToStageCoordinates(stageHitScratch);
        Actor hit = stage.hit(stageHitScratch.x, stageHitScratch.y, true);
        if (hit != null) {
            hoverInBoard = false;
            return;
        }

        int[] cell = mapper.toCell(screenX, screenY);
        if (cell == null) {
            hoverInBoard = false;
            return;
        }
        hoverCellX = cell[0];
        hoverCellY = cell[1];
        hoverInBoard = true;
    }

    public BoardRenderer.Ghost computeGhost() {
        if (!hoverInBoard) return null;
        if (edit.hasActiveEdit()) return null;

        SimulationState s = game.getSimulationState();
        if (s == SimulationState.RUNNING || s == SimulationState.PAUSED) return null;

        Cell cell = game.getBoard().getCell(hoverCellX, hoverCellY);
        if (cell == null || !cell.isEmpty()) return null;

        Component component = hotbar.getActivePalette().createSelected();
        if (component == null) return null;

        return new BoardRenderer.Ghost(hoverCellX, hoverCellY, component);
    }
}
