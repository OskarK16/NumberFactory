package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.components.Component;

public class HotbarController {

    public enum SubCategory {
        ARITHMETIC, LOGIC
    }

    private final ComponentPaletteSelection utility;
    private final ComponentPaletteSelection arithmetic;
    private final ComponentPaletteSelection logic;

    private ComponentPaletteSelection activePalette;
    private SubCategory openSub = null;

    public HotbarController(ComponentPaletteSelection utility, ComponentPaletteSelection arithmetic, ComponentPaletteSelection logic) {
        this.utility = utility;
        this.arithmetic = arithmetic;
        this.logic = logic;
        this.activePalette = utility;
    }

    public ComponentPaletteSelection getUtility() { return utility; }
    public ComponentPaletteSelection getArithmetic() { return arithmetic; }
    public ComponentPaletteSelection getLogic() { return logic; }
    public ComponentPaletteSelection getActivePalette() { return activePalette; }
    public SubCategory getOpenSub() { return openSub; }
    public boolean isSubOpen() { return openSub != null; }

    public void openSub(SubCategory cat) {
        openSub = cat;
    }

    public void closeSub() {
        openSub = null;
    }

    public boolean selectFromUtility(int index) {
        if (!utility.selectByIndex(index)) {
            return false;
        }

        activePalette = utility;
        closeSub();
        return true;
    }

    public boolean selectFromArithmetic(int index) {
        if (!arithmetic.selectByIndex(index)) {
            return false;
        }

        activePalette = arithmetic;
        return true;
    }

    public boolean selectFromLogic(int index) {
        if (!logic.selectByIndex(index)) {
            return false;
        }

        activePalette = logic;
        return true;
    }

    public void selectNext() {
        ComponentPaletteSelection active = getActivePalette();
        int currentIndex = active.getSelectedIndex();

        if (currentIndex < active.getEntries().size() - 1) {
            if (active == utility) {
                selectFromUtility(currentIndex + 1);
            }
            else if (active == arithmetic) {
                selectFromArithmetic(currentIndex + 1);
            }
            else if (active == logic) {
                selectFromLogic(currentIndex + 1);
            }
        } else {
            if (active == utility) {
                openSub(SubCategory.ARITHMETIC);
                selectFromArithmetic(0);
            }
            else if (active == arithmetic) {
                openSub(SubCategory.LOGIC);
                selectFromLogic(0);
            }
            else if (active == logic) {
                closeSub();
                selectFromUtility(0);
            }
        }
    }

    public void selectPrev() {
        ComponentPaletteSelection active = getActivePalette();
        int currentIndex = active.getSelectedIndex();

        if (currentIndex > 0) {
            if (active == utility) {
                selectFromUtility(currentIndex - 1);
            }
            else if (active == arithmetic) {
                selectFromArithmetic(currentIndex - 1);
            }
            else if (active == logic) {
                selectFromLogic(currentIndex - 1);
            }
        }
        else {
            if (active == utility) {
                openSub(SubCategory.LOGIC);
                selectFromLogic(logic.getEntries().size() - 1);
            }
            else if (active == arithmetic) {
                closeSub();
                selectFromUtility(utility.getEntries().size() - 1);
            }
            else if (active == logic) {
                openSub(SubCategory.ARITHMETIC);
                selectFromArithmetic(arithmetic.getEntries().size() - 1);
            }
        }
    }

    public Component createSelected() {
        return activePalette.createSelected();
    }
}
