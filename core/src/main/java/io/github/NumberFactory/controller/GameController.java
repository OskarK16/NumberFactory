package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.*;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Constants;

import java.util.List;

public class GameController {

    private final Level level;
    private float tickAccumulator = 0f;

    public GameController(Level level) {
        this.level = level;
    }

    public void update(float dt) {
        if (!level.getMachine().isRunning()) {
            return;
        }

        tickAccumulator += dt;
        while (tickAccumulator >= Constants.SIM_TICK_DURATION) {
            tickAccumulator -= Constants.SIM_TICK_DURATION;
            level.getMachine().tick();
        }
    }

    public float getTickProgress() {
        SimulationState s = level.getMachine().getState();
        if (s == SimulationState.IDLE) {
            return 0f;
        }
        if (s == SimulationState.COMPLETED) {
            return 1f;
        }

        float p = tickAccumulator / Constants.SIM_TICK_DURATION;
        if (p < 0f) {
            return 0f;
        }
        if (p > 1f) {
            return 1f;
        }
        return p;
    }

    public Level getLevel() {
        return level;
    }

    public Machine getMachine() {
        return level.getMachine();
    }

    public Board getBoard() {
        return level.getMachine().getBoard();
    }

    public SimulationState getSimulationState() {
        return level.getMachine().getState();
    }

    public Inventory getInventory() {
        return level.getInventory();
    }

    public Goal getGoal() {
        return level.getGoal();
    }

    public List<Integer> getAggregated() {
        return level.getMachine().getAggregated();
    }

    public boolean placeComponent(int x, int y, Component c) {
        return level.placeComponent(x, y, c);
    }

    public boolean removeComponent(int x, int y) {
        return level.removeComponent(x, y);
    }

    public boolean start() {
        Machine machine = level.getMachine();
        if (machine.hasItemsInFlight()) {
            return false;
        }

        machine.reset();
        resetTickAccumulator();
        return machine.start();
    }

    public boolean pause() {
        return level.getMachine().pause();
    }

    public boolean resume() {
        return level.getMachine().resume();
    }

    public boolean reset() {
        resetTickAccumulator();
        return level.getMachine().reset();
    }

    public boolean restart() {
        resetTickAccumulator();
        return level.getMachine().restart();
    }

    private void resetTickAccumulator() {
        tickAccumulator = 0f;
    }

    public SimulationLogger getActionLogger() {
        return level.getMachine().getActionLogger();
    }
}
