package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.Level;
import io.github.NumberFactory.save.LevelDefinitionMapper;
import io.github.NumberFactory.save.LevelSaveMapper;
import io.github.NumberFactory.save.SaveManager;
import io.github.NumberFactory.save.data.LevelDefinitionData;

public class LevelController {
    private final boolean isSandbox;
    private final int levelNumber;
    private final String levelName;
    private final String playerAlias;
    private String saveName;
    private String saveSlot;

    private String description = "";
    private String author = "";

    private final SaveManager saveManager = new SaveManager();
    private final LevelDefinitionMapper defMapper  = new LevelDefinitionMapper();
    private final LevelSaveMapper saveMapper = new LevelSaveMapper();

    private Level level;

    public LevelController(boolean isSandbox, int levelNumber, String levelName, String playerAlias, String saveSlot) {
        this.isSandbox = isSandbox;
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.playerAlias = playerAlias;
        this.saveSlot = isSandbox ? saveSlot : null;
        this.saveName = isSandbox
            ? "sbox_" + playerAlias + "_" + saveSlot
            : "camp_" + playerAlias + "_" + levelName;
        this.level = null;
    }

    public Level getLevel() {
        if (level == null) loadFreshLevel();
        return level;
    }

    public void loadSave() {
        loadFreshLevel();
        saveMapper.applyTo(level, saveManager.loadData(saveName));
    }

    public void loadFreshLevel() {
        if (isSandbox) {
            this.level = Level.sandbox();
            return;
        }
        if (!saveManager.hasDefinition(levelName)) {
            this.level = Level.sandbox();
            return;
        }
        LevelDefinitionData data = saveManager.loadDefinition(levelName);
        this.description = data.metadata.description != null ? data.metadata.description : "";
        this.author = data.metadata.author != null ? data.metadata.author : "";
        this.level = defMapper.fromDefinition(data);
    }

    public void saveLevel() {
        saveManager.saveData(saveMapper.toSaveData(level, levelName), saveName);
    }

    public void saveLevelDefinition() {
        LevelDefinitionData data = defMapper.toDefinition(level, levelName);
        data.metadata.description = description;
        data.metadata.author = author;
        saveManager.saveDefinition(data, levelName);
    }

    public boolean isSandbox() { return isSandbox; }
    public int getLevelNumber() { return levelNumber; }
    public String getLevelName() { return levelName; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getSaveSlot() { return saveSlot; }

    public void setDescription(String description) { this.description = description; }
    public void setAuthor(String author) { this.author = author; }

    public void setSaveSlot(String newSlot) {
        if (!isSandbox) return;
        this.saveSlot = newSlot;
        this.saveName = "sbox_" + playerAlias + "_" + newSlot;
    }
}
