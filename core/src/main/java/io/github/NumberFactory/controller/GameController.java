package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.*;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.utility.OutputComponent;
import io.github.NumberFactory.utils.Constants;

import java.util.List;

public class GameController {

    private final Level level;
    private float tickAccumulator = 0f;
    private final SequenceGoal campaignGoal;

    public GameController(Level level, SequenceGoal campaignGoal) {
        this.level = level;
        this.campaignGoal = campaignGoal;
    }

    public void update(float dt) {
        if (!level.getMachine().isRunning()) {
            return;
        }

        tickAccumulator += dt;
        while (tickAccumulator >= Constants.SIM_TICK_DURATION) {
            tickAccumulator -= Constants.SIM_TICK_DURATION;
            level.getMachine().tick();

            SimulationLogger logger = level.getMachine().getActionLogger();
            Board board = level.getMachine().getBoard();

            for (int x = 0; x < board.width; x++) {
                for (int y = 0; y < board.height; y++) {
                    Component c = board.getCell(x, y).getComponent();
                    if (c instanceof OutputComponent) {
                        List<Item> logItems = ((OutputComponent) c).takeLogs();

                        for (Item item : logItems) {
                            if (logger != null) {
                                logger.log("Returned " + item.getValue());
                            }

                            if (campaignGoal != null && !campaignGoal.isFailed() && !campaignGoal.isComplete()) {
                                campaignGoal.submitValue(item.getValue());
                                if (campaignGoal.isComplete()) {
                                    pause();
                                }
                            }
                        }
                    }
                }
            }
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

    public SequenceGoal getCampaignGoal() {
        return campaignGoal;
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

        if (campaignGoal != null) {
            campaignGoal.reset();
        }

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

        if (campaignGoal != null) {
            campaignGoal.reset();
        }

        return level.getMachine().reset();
    }

    public boolean restart() {
        resetTickAccumulator();

        if (campaignGoal != null) {
            campaignGoal.reset();
        }

        return level.getMachine().restart();
    }

    private void resetTickAccumulator() {
        tickAccumulator = 0f;
    }

    public SimulationLogger getActionLogger() {
        return level.getMachine().getActionLogger();
    }
}
