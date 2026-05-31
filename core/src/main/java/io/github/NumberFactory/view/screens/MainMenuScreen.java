package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.NumberFactory.Main;

public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font, BitmapFont.class);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = new Color(0.95f, 0.75f, 0.20f, 1f);
        skin.add("title", titleStyle);

        Label.LabelStyle standardTextStyle = new Label.LabelStyle();
        standardTextStyle.font = font;
        standardTextStyle.fontColor = Color.WHITE;
        skin.add("default", standardTextStyle);

        createColorButtonStyle("btn-blue", 0.25f, 0.60f, 0.85f, font);
        createColorButtonStyle("btn-yellow", 0.85f, 0.65f, 0.20f, font);
        createColorButtonStyle("btn-purple", 0.60f, 0.35f, 0.80f, font);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("NUMBER FACTORY", skin, "title");
        title.setFontScale(3.5f);
        title.setAlignment(Align.center);

        TextButton sandbox = new TextButton("Sandbox", skin, "btn-blue");
        TextButton campaign = new TextButton("Campaign (soon)", skin, "btn-yellow");
        TextButton instructions = new TextButton("Instructions", skin, "btn-purple");

        table.add(title).padBottom(80).row();
        table.add(sandbox).width(240).height(55).padBottom(20).row();
        table.add(campaign).width(240).height(55).padBottom(20).row();
        table.add(instructions).width(240).height(55).row();

        sandbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelScreen(game, 0));
            }
        });

        campaign.setDisabled(true);

        instructions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InstructionsScreen(game));
            }
        });

        stage.addActor(table);
    }

    private void createColorButtonStyle(String name, float r, float g, float b, BitmapFont font) {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        pix.setColor(r, g, b, 1f); pix.fill();
        skin.add(name + "-up", new Texture(pix));

        pix.setColor(Math.min(r + 0.15f, 1f), Math.min(g + 0.15f, 1f), Math.min(b + 0.15f, 1f), 1f); pix.fill();
        skin.add(name + "-over", new Texture(pix));

        pix.setColor(Math.max(r - 0.15f, 0f), Math.max(g - 0.15f, 0f), Math.max(b - 0.15f, 0f), 1f); pix.fill();
        skin.add(name + "-down", new Texture(pix));

        pix.dispose();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.overFontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(new TextureRegion(skin.get(name + "-up", Texture.class)));
        style.over = new TextureRegionDrawable(new TextureRegion(skin.get(name + "-over", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(skin.get(name + "-down", Texture.class)));

        skin.add(name, style);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.10f, 0.14f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
