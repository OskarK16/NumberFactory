package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.NumberFactory.Main;
import io.github.NumberFactory.view.render.MenuTheme;

public abstract class MenuScreen implements Screen {

    protected final Main app;
    protected Stage stage;
    protected MenuTheme theme;

    protected MenuScreen(Main app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        theme = new MenuTheme();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(theme.background());
        stage.addActor(theme.grid());
        build();
    }

    protected abstract void build();

    protected void addFooter() {
        Label footer = new Label("Dominik  ·  Józef  ·  Oskar", theme.skin(), "dim");
        footer.setFontScale(0.7f);
        Table bar = new Table();
        bar.setFillParent(true);
        bar.bottom();
        bar.add(footer).padBottom(18);
        stage.addActor(bar);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(theme.bgBottom.r, theme.bgBottom.g, theme.bgBottom.b, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
        theme.dispose();
    }
}
