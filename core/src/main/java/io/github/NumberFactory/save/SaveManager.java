package io.github.NumberFactory.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import io.github.NumberFactory.save.data.LevelDefinitionData;
import io.github.NumberFactory.save.data.LevelSaveData;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final String ROOT_DIR   = "data/savesystem";
    private static final String SAVE_DIR   = ROOT_DIR + "/saves";
    private static final String LEVELS_DIR = ROOT_DIR + "/levels";
    private static final String ALIASES    = ROOT_DIR + "/aliases.json";

    private final Json json = new Json();

    public void saveDefinition(LevelDefinitionData data, String levelName) {
        String text = json.toJson(data);
        Gdx.files.local(LEVELS_DIR + "/" + levelName + ".json").writeString(text, false);
    }

    public boolean hasDefinition(String levelName) {
        return Gdx.files.local(LEVELS_DIR + "/" + levelName + ".json").exists();
    }

    public LevelDefinitionData loadDefinition(String levelName) {
        if (!hasDefinition(levelName)) return null;
        return json.fromJson(
            LevelDefinitionData.class,
            Gdx.files.local(LEVELS_DIR + "/" + levelName + ".json").readString()
        );
    }

    public List<String> listDefinitions() {
        List<String> names = new ArrayList<>();
        FileHandle dir = Gdx.files.local(LEVELS_DIR);
        if (!dir.exists()) return names;
        for (FileHandle file : dir.list(".json")) {
            names.add(file.nameWithoutExtension());
        }
        return names;
    }

    public void saveData(LevelSaveData data, String saveId) {
        String text = json.toJson(data);
        Gdx.files.local(SAVE_DIR + "/" + saveId + ".json").writeString(text, false);
    }

    public LevelSaveData loadData(String saveId) {
        if (!hasSave(saveId)) return null;
        return json.fromJson(
            LevelSaveData.class,
            Gdx.files.local(SAVE_DIR + "/" + saveId + ".json").readString()
        );
    }

    public boolean hasSave(String saveId) {
        return Gdx.files.local(SAVE_DIR + "/" + saveId + ".json").exists();
    }

    public List<String> listSandboxSaves() {
        return listSavesMatching("sbox_");
    }

    public List<String> listSandboxSaves(String alias) {
        return listSavesMatching("sbox_" + alias + "_");
    }

    public List<String> ListSandboxSaves(String alias, String saveName) {
        //for now just looks for matching prefix, I want to implement fuzzy search later
        return listSavesMatching("sbox_" + alias + "_" + saveName);
    }

    public List<String> listLevelSaves() {
        return listSavesMatching("camp_");
    }

    public List<String> listLevelSaves(String alias) {
        return listSavesMatching("camp_" + alias + "_");
    }

    public List<String> ListLevelSaves(String alias, String levelName) {
        return listSavesMatching("camp_" + alias + "_" + levelName);
    }

    public void saveAlias(String alias) {
        List<String> existing = listAliases();
        if (existing.contains(alias)) return;
        existing.add(alias);
        Gdx.files.local(ALIASES).writeString(json.toJson(existing), false);
    }

    @SuppressWarnings("unchecked")
    public List<String> listAliases() {
        FileHandle file = Gdx.files.local(ALIASES);
        if (!file.exists()) return new ArrayList<>();
        return json.fromJson(ArrayList.class, String.class, file.readString());
    }

    private List<String> listSavesMatching(String prefix) {
        List<String> names = new ArrayList<>();
        FileHandle dir = Gdx.files.local(SAVE_DIR);
        if (!dir.exists()) return names;
        for (FileHandle file : dir.list(".json")) {
            if (file.nameWithoutExtension().startsWith(prefix)) {
                names.add(file.nameWithoutExtension());
            }
        }
        return names;
    }
}
