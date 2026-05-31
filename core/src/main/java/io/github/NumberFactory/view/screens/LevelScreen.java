package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.NumberFactory.Main;
import io.github.NumberFactory.controller.ComponentPaletteSelection;
import io.github.NumberFactory.controller.EditController;
import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.controller.InputHandler;
import io.github.NumberFactory.controller.ScreenToCellMapper;
import io.github.NumberFactory.model.Level;
import io.github.NumberFactory.model.SimulationState;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.arithmetic.AddComponent;
import io.github.NumberFactory.model.components.arithmetic.DivideComponent;
import io.github.NumberFactory.model.components.arithmetic.ModuloComponent;
import io.github.NumberFactory.model.components.arithmetic.MultiplyComponent;
import io.github.NumberFactory.model.components.arithmetic.SubtractComponent;
import io.github.NumberFactory.model.components.logic.EqualsComponent;
import io.github.NumberFactory.model.components.logic.GreaterOrEqualComponent;
import io.github.NumberFactory.model.components.logic.GreaterThanComponent;
import io.github.NumberFactory.model.components.logic.LessOrEqualComponent;
import io.github.NumberFactory.model.components.logic.LessThanComponent;
import io.github.NumberFactory.model.components.logic.NotEqualsComponent;
import io.github.NumberFactory.model.components.utility.CopyComponent;
import io.github.NumberFactory.model.components.utility.DestroyerComponent;
import io.github.NumberFactory.model.components.utility.GeneratorComponent;
import io.github.NumberFactory.model.components.utility.OutputComponent;
import io.github.NumberFactory.model.components.utility.TransportComponent;
import io.github.NumberFactory.utils.Constants;
import io.github.NumberFactory.view.gui.EditPanel;
import io.github.NumberFactory.view.gui.Hotbar;
import io.github.NumberFactory.view.gui.Sidebar;
import io.github.NumberFactory.view.gui.SubHotbar;
import io.github.NumberFactory.view.gui.TopBar;
import io.github.NumberFactory.view.render.BoardRenderer;
import io.github.NumberFactory.view.render.CameraController;
import io.github.NumberFactory.view.render.ScreenToCellMapperImpl;
import io.github.NumberFactory.view.render.TextureRegistry;
import io.github.NumberFactory.view.render.UiSkin;

public class LevelScreen implements Screen {

    private final Main app;
    private final int levelNumber;

    private Level level;
    private GameController game;
    private EditController edit;
    private HotbarController hotbar;
    private InputHandler input;
    private ScreenToCellMapper mapper;

    private OrthographicCamera gameCamera;
    private TextureRegistry textures;
    private BoardRenderer boardRenderer;
    private CameraController cameraController;

    private Stage stage;
    private Table root;
    private Skin skin;
    private TopBar topBar;
    private Hotbar hotbarView;
    private SubHotbar subHotbarView;
    private EditPanel editPanel;
    private Sidebar sidebar;

    private int hoverCellX  = -1;
    private int hoverCellY  = -1;
    private boolean hoverInBoard = false;
    private final Vector2 stageHitScratch = new Vector2();

    private static final float PANEL_MARGIN = 8f;
    private static final float PANEL_GAP    = 8f;

    public LevelScreen(Main app, int levelNumber) {
        this.app = app;
        this.levelNumber = levelNumber;
    }

    @Override
    public void show() {
        level = Level.sandbox();
        game = new GameController(level);
        edit = new EditController(game.getBoard());
        hotbar = buildHotbar();

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameCamera.position.set(level.getMachine().getBoard().width * Constants.TILE_SIZE / 2f, level.getMachine().getBoard().height * Constants.TILE_SIZE / 2f, 0);
        gameCamera.update();

        textures = new TextureRegistry();
        boardRenderer = new BoardRenderer(gameCamera, textures);
        mapper = new ScreenToCellMapperImpl(gameCamera, game.getBoard());
        input = new InputHandler(game, edit, hotbar, mapper);
        cameraController = new CameraController(gameCamera);

        skin = UiSkin.build();
        stage = new Stage(new ScreenViewport());
        buildUi();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputAdapter() {
            @Override public boolean mouseMoved(int sx, int sy) {
                updateHover(sx, sy); return false;
            }
            @Override public boolean touchDragged(int sx, int sy, int p) {
                updateHover(sx, sy); return false;
            }
        });
        multiplexer.addProcessor(stage);
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

    private HotbarController buildHotbar() {
        ComponentPaletteSelection utility = new ComponentPaletteSelection();
        utility.add(TransportComponent.class, TransportComponent::new);
        utility.add(GeneratorComponent.class, () -> new GeneratorComponent(1));
        utility.add(DestroyerComponent.class, DestroyerComponent::new);
        utility.add(OutputComponent.class, OutputComponent::new);
        utility.add(CopyComponent.class, CopyComponent::new);

        ComponentPaletteSelection arithmetic = new ComponentPaletteSelection();
        arithmetic.add(AddComponent.class, AddComponent::new);
        arithmetic.add(SubtractComponent.class, SubtractComponent::new);
        arithmetic.add(MultiplyComponent.class, MultiplyComponent::new);
        arithmetic.add(DivideComponent.class, DivideComponent::new);
        arithmetic.add(ModuloComponent.class, ModuloComponent::new);

        ComponentPaletteSelection logic = new ComponentPaletteSelection();
        logic.add(EqualsComponent.class, EqualsComponent::new);
        logic.add(NotEqualsComponent.class, NotEqualsComponent::new);
        logic.add(GreaterThanComponent.class, GreaterThanComponent::new);
        logic.add(GreaterOrEqualComponent.class, GreaterOrEqualComponent::new);
        logic.add(LessThanComponent.class, LessThanComponent::new);
        logic.add(LessOrEqualComponent.class, LessOrEqualComponent::new);

        return new HotbarController(utility, arithmetic, logic);
    }

    private void buildUi() {
        topBar = new TopBar(game, skin);
        hotbarView = new Hotbar(hotbar, level.getInventory(), textures, skin);
        subHotbarView = new SubHotbar(hotbar, textures, skin);
        editPanel = new EditPanel(edit, game, skin);
        sidebar = new Sidebar(game, skin);

        root = new Table();
        root.setFillParent(true);
        root.top();

        root.add(topBar).growX().colspan(2).row();
        root.add(sidebar).top().left().width(180).padTop(8).padLeft(8);
        root.add().expand().row();

        root.add(subHotbarView).colspan(2).padBottom(4).row();
        root.add(hotbarView).colspan(2).padBottom(8).row();
        stage.addActor(root);

        editPanel.setVisible(false);
        editPanel.pack();
        stage.addActor(editPanel);
    }

    @Override
    public void render(float dt) {
        game.update(dt);

        ScreenUtils.clear(0.066f, 0.075f, 0.094f, 1f);

        boardRenderer.render(game.getBoard(), game.getTickProgress(), computeGhost());

        topBar.update();
        hotbarView.update();
        subHotbarView.update();
        editPanel.update();
        sidebar.update();

        stage.act(dt);
        positionEditPanel();
        stage.draw();
    }

    private void positionEditPanel() {
        if (!editPanel.isVisible()) {
            return;
        }

        root.validate();
        editPanel.pack();
        float x = stage.getWidth() - editPanel.getWidth() - PANEL_MARGIN;
        float y = sidebar.getY() - PANEL_GAP - editPanel.getHeight();
        editPanel.setPosition(x, y);
    }

    private void updateHover(int screenX, int screenY) {
        stageHitScratch.set(screenX, screenY);
        stage.screenToStageCoordinates(stageHitScratch);
        Actor hit = stage.hit(stageHitScratch.x, stageHitScratch.y, true);
        if (hit != null) {
            hoverInBoard = false;
            return;
        }

        int[] cell = mapper.toCell(screenX, screenY);
        if (cell == null) {
            hoverInBoard = false;
            return;
        }
        hoverCellX = cell[0];
        hoverCellY = cell[1];
        hoverInBoard = true;
    }

    private BoardRenderer.Ghost computeGhost() {
        if (!hoverInBoard) {
            return null;
        }
        if (edit.hasActiveEdit()) {
            return null;
        }

        SimulationState s = game.getSimulationState();
        if (s == SimulationState.RUNNING || s == SimulationState.PAUSED) {
            return null;
        }
        Cell cell = game.getBoard().getCell(hoverCellX, hoverCellY);

        if (cell == null || !cell.isEmpty()) {
            return null;
        }

        Class<? extends Component> type = hotbar.getActivePalette().getSelectedType();
        if (type == null) {
            return null;
        }

        return new BoardRenderer.Ghost(hoverCellX, hoverCellY, type);
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        gameCamera.viewportWidth = width;
        gameCamera.viewportHeight = height;
        gameCamera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause()  {
    }
    @Override public void resume() {
    }
    @Override public void hide()   {
    }

    @Override
    public void dispose() {
        if (boardRenderer != null) {
            boardRenderer.dispose();
        }
        if (textures != null) {
            textures.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }
}
