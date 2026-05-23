package io.github.NumberFactory.model;

import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.utility.OutputComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Machine {
    private final Board board;
    private SimulationState state = SimulationState.IDLE;
    private Goal goal;
    private final List<Integer> aggregated = new ArrayList<>();

    public Machine(Board board) {
        this.board = board;
    }

    public Machine(int x, int y) {
        this.board = new Board(x, y);
    }

    public Board getBoard() { return board; }
    public SimulationState getState() { return state; }
    public boolean isRunning()   { return state == SimulationState.RUNNING; }
    public boolean isCompleted() { return state == SimulationState.COMPLETED; }
    public boolean isIdle()      { return state == SimulationState.IDLE; }
    public boolean isPaused()    { return state == SimulationState.PAUSED; }

    public boolean hasItemsInFlight() {
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Component c = board.getCell(x, y).getComponent();
                if (c != null && !c.getHeldItems().isEmpty()) return true;
            }
        }
        return false;
    }

    public boolean isRunComplete() {
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Component c = board.getCell(x, y).getComponent();
                if (c == null) continue;
                if (!c.isExhausted()) return false;
                if (!c.getHeldItems().isEmpty()) return false;
            }
        }
        return true;
    }

    public Goal getGoal() { return goal; }
    public void setGoal(Goal goal) { this.goal = goal; }
    public List<Integer> getAggregated() { return Collections.unmodifiableList(aggregated); }

    public boolean start() {
        if (state == SimulationState.RUNNING) return false;
        if (board.hasEditingCell()) return false;

        boolean allStrict = true;
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Cell cell = board.getCell(x, y);
                if (cell.isEmpty()) continue;
                if (cell.isInEdit()) return false;
                if (!cell.getComponent().isStrictlyValid()) {
                    board.markInvalid(x, y);
                    allStrict = false;
                }
            }
        }
        if (!allStrict) return false;
        state = SimulationState.RUNNING;
        return true;
    }

    public boolean pause() {
        if (state != SimulationState.RUNNING) return false;
        state = SimulationState.PAUSED;
        return true;
    }

    public boolean resume() {
        if (state != SimulationState.PAUSED) return false;
        state = SimulationState.RUNNING;
        return true;
    }

    public boolean reset() {
        if (board.hasEditingCell()) return false;
        for (int x = 0; x < board.width; x++)
            for (int y = 0; y < board.height; y++)
                board.getCell(x, y).reset();
        aggregated.clear();
        state = SimulationState.IDLE;
        return true;
    }

    public boolean restart() {
        if (board.hasEditingCell()) return false;
        board.clear();
        aggregated.clear();
        state = SimulationState.IDLE;
        return true;
    }

    public void tick() {
        if (state != SimulationState.RUNNING) return;
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Component c = board.getCell(x, y).getComponent();
                if (c != null) c.computeTick();
            }
        }
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Component c = board.getCell(x, y).getComponent();
                if (c != null) c.applyTick();
            }
        }
        if (goal != null) processGoalTick();
        if (state == SimulationState.RUNNING && isRunComplete()) {
            state = SimulationState.IDLE;
        }
    }

    private void processGoalTick() {
        List<Integer> fresh = new ArrayList<>();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Component c = board.getCell(x, y).getComponent();
                if (c instanceof OutputComponent oc) {
                    for (var item : oc.takeFresh()) fresh.add(item.getValue());
                }
            }
        }
        if (fresh.isEmpty()) return;
        Collections.sort(fresh);
        aggregated.addAll(fresh);
        if (!goal.isPrefix(aggregated)) {
            aggregated.clear();
            return;
        }
        if (goal.isMet(aggregated)) {
            state = SimulationState.COMPLETED;
        }
    }
}
