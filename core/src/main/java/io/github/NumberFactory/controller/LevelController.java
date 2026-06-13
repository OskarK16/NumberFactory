package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.Level;
import io.github.NumberFactory.save.LevelDefinitionMapper;
import io.github.NumberFactory.save.LevelSaveMapper;
import io.github.NumberFactory.save.SaveManager;
import io.github.NumberFactory.model.SequenceGoal;
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

    private final LevelDefinitionMapper defMapper  = new LevelDefinitionMapper();
    private final LevelSaveMapper saveMapper = new LevelSaveMapper();

    private Level level;
    private SequenceGoal goal;

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
        saveMapper.applyTo(level, SaveManager.loadData(saveName));
    }

    public SequenceGoal getGoal() {
        return goal;
    }

    public void loadFreshLevel() {
        if (isSandbox) {
            this.level = Level.sandbox();
            this.goal = null;
            return;
        }

        if (!SaveManager.hasDefinition(levelName)) {
            this.level = Level.sandbox();
            this.goal = null;
            return;
        }

        LevelDefinitionData data = SaveManager.loadDefinition(levelName);
        this.description = data.metadata.description != null ? data.metadata.description : "";
        this.author = data.metadata.author != null ? data.metadata.author : "";
        this.level = defMapper.fromDefinition(data);

        if (data.goal != null && !data.goal.isEmpty()) {
            this.goal = new SequenceGoal(levelName.toUpperCase(), this.description, data.goal);
        }
    }
    public void saveLevel() {
        SaveManager.saveData(saveMapper.toSaveData(this, levelName), saveName);
    }
}
