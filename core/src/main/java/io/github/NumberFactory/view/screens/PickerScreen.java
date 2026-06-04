package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import io.github.NumberFactory.Main;
import io.github.NumberFactory.view.gui.ChoosableList;

import java.util.List;


public abstract class PickerScreen extends MenuScreen {

    protected PickerScreen(Main app) {
        super(app);
    }

    protected abstract String title();
    protected abstract String subtitle();
    protected abstract int cardWidth();
    protected abstract List<String> items();
    protected abstract String selectLabel();
    protected abstract void onSelect(String item);
    protected abstract String inputPlaceholder();
    protected abstract String createLabel();
    protected abstract void onCreate(String name);

    protected String detail(String item) {
        return null;
    }

    protected String display(String item) {
        return item;
    }

    protected void onDelete(String item) {
    }

    protected boolean deletable() {
        return false;
    }

    @Override
    protected void build() {
        Table card = theme.card();

        card.add(theme.title(title(), 2.0f, 48)).padBottom(6).row();
        Label subtitle = new Label(subtitle(), theme.skin(), "dim");
        subtitle.setFontScale(0.8f);
        subtitle.setAlignment(Align.center);
        card.add(subtitle).padBottom(28).row();

        ChoosableList list = new ChoosableList(theme, cardWidth(), this::items)
            .display(this::display)
            .detail(this::detail)
            .onSelect(selectLabel(), this::onSelect)
            .onAdd(createLabel(), inputPlaceholder(), this::onCreate);
        if (deletable()) {
            list.onDelete(this::onDelete);
        }
        card.add(list.refresh()).padBottom(20).row();

        TextButton back = theme.menuButton("BACK", theme.purple, false);
        back.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        card.add(back).width(cardWidth()).height(52).row();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(card);
        stage.addActor(root);

        addFooter();
    }
}
