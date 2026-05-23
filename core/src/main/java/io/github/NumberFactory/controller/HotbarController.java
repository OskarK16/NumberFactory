package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.components.Component;

public class HotbarController {

    public enum SubCategory { ARITHMETIC, LOGIC }

    private final ComponentPaletteSelection utility;
    private final ComponentPaletteSelection arithmetic;
    private final ComponentPaletteSelection logic;

    private ComponentPaletteSelection activePalette;
    private SubCategory openSub = null;

    public HotbarController(ComponentPaletteSelection utility,
                            ComponentPaletteSelection arithmetic,
                            ComponentPaletteSelection logic) {
        this.utility = utility;
        this.arithmetic = arithmetic;
        this.logic = logic;
        this.activePalette = utility;
    }

    public ComponentPaletteSelection getUtility()        { return utility; }
    public ComponentPaletteSelection getArithmetic()     { return arithmetic; }
    public ComponentPaletteSelection getLogic()          { return logic; }
    public ComponentPaletteSelection getActivePalette()  { return activePalette; }
    public SubCategory getOpenSub()                      { return openSub; }
    public boolean isSubOpen()                           { return openSub != null; }

    public void openSub(SubCategory cat) {
        openSub = cat;
    }

    public void closeSub() {
        openSub = null;
    }

    public boolean selectFromUtility(int index) {
        if (!utility.selectByIndex(index)) return false;
        activePalette = utility;
        closeSub();
        return true;
    }

    public boolean selectFromArithmetic(int index) {
        if (!arithmetic.selectByIndex(index)) return false;
        activePalette = arithmetic;
        return true;
    }

    public boolean selectFromLogic(int index) {
        if (!logic.selectByIndex(index)) return false;
        activePalette = logic;
        return true;
    }

    public void selectNext() {
        activePalette.selectNext();
    }

    public void selectPrev() {
        activePalette.selectPrev();
    }

    public Component createSelected() {
        return activePalette.createSelected();
    }
}
