package io.github.NumberFactory.model;

import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Constants;

public final class Level {
    private final Machine machine;
    private final Inventory inventory;
    private final Goal goal;
    private final GameMode mode;

    private Level(Machine machine, Inventory inventory, Goal goal, GameMode mode) {
        this.machine = machine;
        this.inventory = inventory;
        this.goal = goal;
        this.mode = mode;
        if (goal != null) machine.setGoal(goal);
    }

    public static Level sandbox(Machine machine) {
        return new Level(machine, Inventory.unlimited(), null, GameMode.SANDBOX);
    }

    public static Level sandbox() {
        return sandbox(new Machine(new Board(Constants.SANDBOX_WIDTH, Constants.SANDBOX_HEIGHT)));
    }

    public static Level campaign(Machine machine, Inventory inventory, Goal goal) {
        if (inventory == null) throw new IllegalArgumentException("campaign requires inventory");
        if (goal == null)      throw new IllegalArgumentException("campaign requires goal");
        return new Level(machine, inventory, goal, GameMode.CAMPAIGN);
    }

    public Machine getMachine()     { return machine; }
    public Inventory getInventory() { return inventory; }
    public Goal getGoal()           { return goal; }
    public GameMode getMode()       { return mode; }

    public boolean placeComponent(int x, int y, Component c) {
        if (c == null) return false;
        if (!inventory.take(c.getClass())) return false;
        if (!machine.getBoard().placeIntoCell(x, y, c)) {
            inventory.give(c.getClass());
            return false;
        }
        return true;
    }

    public boolean removeComponent(int x, int y) {
        Component c = machine.getBoard().getCell(x, y).getComponent();
        if (c == null) return false;
        Class<? extends Component> type = c.getClass();
        if (!machine.getBoard().clearCell(x, y)) return false;
        inventory.give(type);
        return true;
    }
}
