package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.NumberFactory.Main;
import io.github.NumberFactory.model.board.Board;

public class FirstScreen implements Screen {
    private static final int CELL_SIZE = 64;

    private final Main game;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Board board;
    private SpriteBatch batch;
    private BitmapFont font;

    public FirstScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        board = new Board(20, 20);
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.LIGHT_GRAY);
        camera.position.set(board.width * CELL_SIZE / 2f,board.height * CELL_SIZE / 2f,0
        );
        camera.update();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1f);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                shapeRenderer.rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "ESC - menu", 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
