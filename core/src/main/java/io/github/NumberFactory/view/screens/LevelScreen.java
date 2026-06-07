package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.NumberFactory.Main;
import io.github.NumberFactory.controller.*;
import io.github.NumberFactory.utils.Constants;
import io.github.NumberFactory.view.gui.LevelHud;
import io.github.NumberFactory.view.render.BoardHoverTracker;
import io.github.NumberFactory.view.render.BoardRenderer;
import io.github.NumberFactory.view.render.CameraController;
import io.github.NumberFactory.view.render.ScreenToCellMapperImpl;
import io.github.NumberFactory.view.render.TextureRegistry;

public class LevelScreen implements Screen {

    private static final float AUTOSAVE_INTERVAL = 60f;

    private final Main app;
    private final LevelController levelController;
    private float autoSaveTimer = 0f;

    private GameController game;
    private EditController edit;
    private HotbarController hotbar;
    private InputHandler input;
    private ScreenToCellMapper mapper;

    private OrthographicCamera gameCamera;
    private TextureRegistry textures;
    private BoardRenderer boardRenderer;
    private CameraController cameraController;

    private LevelHud hud;
    private BoardHoverTracker hover;

    public LevelScreen(Main app, LevelController levelController) {
        this.app = app;
        this.levelController = levelController;
    }

    @Override
    public void show() {
        levelController.loadSave();
        game = new GameController(levelController.getLevel(), levelController.getGoal());
        edit = new EditController(game.getBoard());
        hotbar = HotbarFactory.buildDefault();

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameCamera.position.set(
            levelController.getLevel().getMachine().getBoard().width  * Constants.TILE_SIZE / 2f,
            levelController.getLevel().getMachine().getBoard().height * Constants.TILE_SIZE / 2f,
            0
        );
        gameCamera.update();

        textures = new TextureRegistry();
        boardRenderer = new BoardRenderer(gameCamera, textures);
        mapper = new ScreenToCellMapperImpl(gameCamera, game.getBoard());
        input = new InputHandler(game, edit, hotbar, mapper);
        cameraController = new CameraController(gameCamera);

        hud = new LevelHud(game, edit, hotbar, levelController.getLevel(), textures, levelController::saveLevel,
            () -> {app.setScreen(new MainMenuScreen(app));
        });
        hover = new BoardHoverTracker(hud.getStage(), mapper, game, edit, hotbar);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputAdapter() {
            @Override public boolean mouseMoved(int sx, int sy) {
                hover.updateHover(sx, sy); return false;
            }
            @Override public boolean touchDragged(int sx, int sy, int p) {
                hover.updateHover(sx, sy); return false;
            }
        });
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(input);
        multiplexer.addProcessor(cameraController);
        multiplexer.addProcessor(new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    app.setScreen(new MainMenuScreen(app));
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float dt) {
        game.update(dt);

        autoSaveTimer += dt;
        if (autoSaveTimer >= AUTOSAVE_INTERVAL) {
            autoSaveTimer = 0f;
            levelController.saveLevel();
        }

        ScreenUtils.clear(0.066f, 0.075f, 0.094f, 1f);

        boardRenderer.render(game.getBoard(), game.getTickProgress(), hover.computeGhost());
        hud.update();
        hud.act(dt);
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        gameCamera.viewportWidth = width;
        gameCamera.viewportHeight = height;
        gameCamera.update();
        hud.resize(width, height);
    }

    @Override public void pause()  {
    }
    @Override public void resume() {
    }
    @Override public void hide()   {
        levelController.saveLevel();
    }

    @Override
    public void dispose() {
        if (boardRenderer != null) {
            boardRenderer.dispose();
        }
        if (textures != null) {
            textures.dispose();
        }
        if (hud != null) {
            hud.dispose();
        }
    }
}
