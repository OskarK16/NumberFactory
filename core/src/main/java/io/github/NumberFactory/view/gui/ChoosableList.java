package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.NumberFactory.view.render.MenuTheme;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class ChoosableList extends Table {

    private final MenuTheme theme;
    private final float width;
    private final Supplier<List<String>> source;

    private Function<String, String> display = item -> item;
    private Function<String, String> detail = item -> null;
    private String selectLabel = "SELECT";
    private Consumer<String> onSelect = item -> {};
    private Consumer<String> onDelete;
    private String addLabel = "ADD";
    private String placeholder = "";
    private Consumer<String> onAdd;

    public ChoosableList(MenuTheme theme, float width, Supplier<List<String>> source) {
        this.theme = theme;
        this.width = width;
        this.source = source;
    }

    public ChoosableList display(Function<String, String> display) {
        this.display = display;
        return this;
    }

    public ChoosableList detail(Function<String, String> detail) {
        this.detail = detail;
        return this;
    }

    public ChoosableList onSelect(String label, Consumer<String> action) {
        this.selectLabel = label;
        this.onSelect = action;
        return this;
    }

    public ChoosableList onDelete(Consumer<String> action) {
        this.onDelete = action;
        return this;
    }

    public ChoosableList onAdd(String label, String placeholder, Consumer<String> action) {
        this.addLabel = label;
        this.placeholder = placeholder;
        this.onAdd = action;
        return this;
    }

    public ChoosableList refresh() {
        clearChildren();
        List<String> items = source.get();
        if (!items.isEmpty()) {
            add(theme.scrollPane(rows(items))).width(width).maxHeight(220).padBottom(24).row();
            add(theme.divider()).width(width).height(1).padBottom(20).row();
        }
        if (onAdd != null) {
            add(addRow()).width(width).row();
        }
        return this;
    }

    private Table rows(List<String> items) {
        Table list = new Table();
        list.left();
        for (String item : items) {
            Label name = new Label(display.apply(item), theme.skin());
            name.setColor(theme.text);

            String detailText = detail.apply(item);
            list.add(name).left().expandX().padRight(detailText == null ? 24 : 16);
            if (detailText != null) {
                Label detailLabel = new Label(detailText, theme.skin(), "dim");
                detailLabel.setFontScale(0.75f);
                list.add(detailLabel).right().padRight(24);
            }

            TextButton select = theme.menuButton(selectLabel, theme.green, false);
            select.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) { onSelect.accept(item); }
            });
            list.add(select).width(110).height(44).padBottom(10);

            if (onDelete != null) {
                TextButton delete = theme.menuButton("X", theme.purple, false);
                delete.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent event, Actor actor) {
                        onDelete.accept(item);
                        refresh();
                    }
                });
                list.add(delete).width(44).height(44).padLeft(8).padBottom(10);
            }
            list.row();
        }
        return list;
    }

    private Table addRow() {
        TextField field = theme.textField(placeholder);
        TextButton add = theme.menuButton(addLabel, theme.green, false);
        add.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String name = field.getText().trim();
                if (!name.isEmpty()) onAdd.accept(name);
            }
        });

        Table row = new Table();
        row.add(field).growX().height(44).padRight(10);
        row.add(add).width(110).height(44);
        return row;
    }
}
