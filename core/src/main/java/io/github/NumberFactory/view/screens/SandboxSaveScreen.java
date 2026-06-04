package io.github.NumberFactory.view.screens;

import io.github.NumberFactory.Main;
import io.github.NumberFactory.controller.LevelController;
import io.github.NumberFactory.save.SaveManager;

import java.util.List;

public class SandboxSaveScreen extends PickerScreen {

    private final SaveManager saveManager = new SaveManager();
    private final String playerAlias;
    private final String prefix;

    public SandboxSaveScreen(Main app, String playerAlias) {
        super(app);
        this.playerAlias = playerAlias;
        this.prefix = "sbox_" + playerAlias + "_";
    }

    @Override protected String title() { return "SANDBOX"; }
    @Override protected String subtitle() { return "choose a save slot"; }
    @Override protected int cardWidth() { return 360; }
    @Override protected List<String> items() { return saveManager.listSandboxSaves(playerAlias); }
    @Override protected String display(String saveId) { return saveId.substring(prefix.length()); }
    @Override protected String selectLabel() { return "LOAD"; }
    @Override protected void onSelect(String saveId) { openLevel(display(saveId)); }
    @Override protected String inputPlaceholder() { return "Enter save name…"; }
    @Override protected String createLabel() { return "NEW"; }
    @Override protected void onCreate(String slot) { openLevel(slot); }
    @Override protected boolean deletable() { return true; }
    @Override protected void onDelete(String saveId) { saveManager.deleteSave(saveId); }

    private void openLevel(String slot) {
        app.setScreen(new LevelScreen(app, new LevelController(true, 0, "sandbox", playerAlias, slot)));
    }
}
