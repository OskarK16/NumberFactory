package io.github.NumberFactory.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class ConfigManager {
    private static final String CONFIG_PATH = "data/config.json";
    private static final String DEFAULT_PLAYER = "player1";

    private final Json json = new Json();

    public ConfigManager() {
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public AppConfig load() {
        FileHandle file = Gdx.files.local(CONFIG_PATH);
        if (!file.exists()) {
            AppConfig config = new AppConfig();
            config.currentPlayer = DEFAULT_PLAYER;
            save(config);
            return config;
        }
        AppConfig config = json.fromJson(AppConfig.class, file.readString());
        if (config.currentPlayer == null || config.currentPlayer.isEmpty()) {
            config.currentPlayer = DEFAULT_PLAYER;
        }
        return config;
    }

    private void save(AppConfig config) {
        Gdx.files.local(CONFIG_PATH).writeString(json.toJson(config), false);
    }

    public void setCurrentPlayer(String player) {
        AppConfig config = load();
        config.currentPlayer = player;
        save(config);
    }
}
