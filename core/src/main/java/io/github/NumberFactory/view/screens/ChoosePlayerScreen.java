package io.github.NumberFactory.view.screens;

import io.github.NumberFactory.Main;
import io.github.NumberFactory.config.ConfigManager;
import io.github.NumberFactory.save.SaveManager;

import java.util.List;

public class ChoosePlayerScreen extends PickerScreen {

    private final ConfigManager configManager = new ConfigManager();

    public ChoosePlayerScreen(Main app) {
        super(app);
    }

    @Override protected String title() { return "CHOOSE PLAYER"; }
    @Override protected String subtitle() { return "select or create a player"; }
    @Override protected int cardWidth() { return 420; }
    @Override protected List<String> items() { return SaveManager.listAliases(); }
    @Override protected String selectLabel() { return "SELECT"; }
    @Override protected void onSelect(String player) { choose(player); }
    @Override protected String inputPlaceholder() { return "New player name…"; }
    @Override protected String createLabel() { return "ADD"; }
    @Override protected boolean deletable() { return true; }
    @Override protected void onDelete(String player) { SaveManager.deleteAlias(player); }

    @Override
    protected void onCreate(String name) {
        SaveManager.saveAlias(name);
        choose(name);
    }

    @Override
    protected String detail(String player) {
        int count = SaveManager.listSandboxSaves(player).size();
        return count + (count == 1 ? " save" : " saves");
    }

    private void choose(String player) {
        configManager.setCurrentPlayer(player);
        app.setScreen(new MainMenuScreen(app));
    }
}
