package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;
import io.github.NumberFactory.view.gui.ComponentInfo.Category;
import io.github.NumberFactory.view.gui.ComponentInfo.Entry;
import io.github.NumberFactory.view.render.TextureRegistry;


public class ComponentGuidePanel extends Table {

    private enum View { CATEGORIES, CATEGORY, PORTS }

    private static final int ICON = 28;
    private static final int PREVIEW = 48;

    private final Skin skin;
    private final TextureRegistry textures;
    private final Table body = new Table();
    private final ScrollPane scroll;

    private float contentWidth = 460f;
    private View view = View.CATEGORIES;
    private Category currentCategory;
    private Table selectedItem;

    public ComponentGuidePanel(TextureRegistry textures, Skin skin) {
        this.skin = skin;
        this.textures = textures;

        BitmapFont guideFont = new BitmapFont();
        skin.add("guide-font", guideFont, BitmapFont.class);
        skin.add("guide", new Label.LabelStyle(guideFont, skin.get("default", Label.LabelStyle.class).fontColor));
        TextButton.TextButtonStyle guideButton = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        guideButton.font = guideFont;
        skin.add("guide", guideButton);

        setBackground(skin.getDrawable("panel"));
        pad(12);

        add(new Label("COMPONENT GUIDE", skin, "guide")).left().padBottom(10).row();

        scroll = new ScrollPane(body);
        scroll.setScrollingDisabled(true, false);
        scroll.setFadeScrollBars(false);
        add(scroll).left().row();

        TextButton close = new TextButton("CLOSE", skin, "guide");
        close.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { setVisible(false); }
        });
        add(close).padTop(12).width(120).height(30);

        showCategories();
    }

    public void layoutFor(float stageWidth, float stageHeight) {
        float w = MathUtils.clamp(stageWidth * 0.45f, 300f, 520f);
        if (w != contentWidth) {
            contentWidth = w;
            rebuild();
        }
        float maxScrollHeight = Math.max(160f, stageHeight * 0.6f);
        getCell(scroll).width(contentWidth).maxHeight(maxScrollHeight);
        invalidateHierarchy();
        pack();
    }

    private void rebuild() {
        switch (view) {
            case CATEGORIES -> showCategories();
            case CATEGORY -> showCategory(currentCategory);
            case PORTS -> showPorts();
        }
    }

    private void showCategories() {
        view = View.CATEGORIES;
        body.clearChildren();
        for (final Category cat : ComponentInfo.categories()) {
            addNavButton(cat.name(), new Runnable() {
                @Override public void run() { showCategory(cat); }
            });
            addParagraph(cat.description());
        }
        addNavButton("Ports", this::showPorts);
        addParagraph(ComponentInfo.portsSummary());
    }

    private void showCategory(Category cat) {
        view = View.CATEGORY;
        currentCategory = cat;
        selectedItem = null;
        body.clearChildren();
        addHeader(cat.name());

        final Stack preview = new Stack();
        final Label desc = new Label("Select a component to see what it does.", skin, "guide");
        desc.setWrap(true);
        desc.setAlignment(Align.topLeft);

        for (final Entry e : cat.entries()) {
            final Table item = new Table();
            item.setBackground(skin.getDrawable("up"));
            item.add(iconFor(e.type())).size(ICON).padRight(8);
            item.add(new Label(e.name(), skin, "guide")).left().expandX().fillX();
            item.addListener(new ClickListener() {
                @Override public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    select(item, e, preview, desc);
                }
            });
            body.add(item).width(contentWidth).height(ICON + 8).left().padBottom(4).row();
        }

        Table detail = new Table();
        detail.add(preview).size(PREVIEW).top().padRight(10);
        detail.add(desc).width(contentWidth - PREVIEW - 10).minHeight(PREVIEW).top().left();
        body.add(detail).left().padTop(10).row();
    }

    private void select(Table item, Entry e, Stack preview, Label desc) {
        if (selectedItem != null) {
            selectedItem.setBackground(skin.getDrawable("up"));
        }
        item.setBackground(skin.getDrawable("selected"));
        selectedItem = item;

        preview.clearChildren();
        preview.add(iconFor(e.type()));
        desc.setText(e.name() + "\n\n" + e.description());
    }

    private void showPorts() {
        view = View.PORTS;
        body.clearChildren();
        addHeader("Ports");

        addParagraph(ComponentInfo.portsDetails());

        Table legend = new Table();
        legend.add(portSwatch(PortType.INPUT_A)).size(ICON).padRight(6);
        legend.add(new Label("Input A - blue, priority", skin, "guide")).left().padRight(16);
        legend.add(portSwatch(PortType.INPUT_B)).size(ICON).padRight(6);
        legend.add(new Label("Input B - red", skin, "guide")).left().row();
        legend.add(portSwatch(PortType.OUTPUT_A)).size(ICON).padRight(6).padTop(6);
        legend.add(new Label("Output A - blue, priority", skin, "guide")).left().padRight(16).padTop(6);
        legend.add(portSwatch(PortType.OUTPUT_B)).size(ICON).padRight(6).padTop(6);
        legend.add(new Label("Output B - red", skin, "guide")).left().padTop(6);
        body.add(legend).left().padTop(4).row();
    }

    private void addHeader(String title) {
        TextButton back = new TextButton("< BACK", skin, "guide");
        back.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { showCategories(); }
        });
        Table header = new Table();
        header.add(back).width(110).height(28).left().padRight(12);
        header.add(new Label(title, skin, "guide")).left();
        body.add(header).left().padBottom(10).row();
    }

    private void addNavButton(String text, Runnable onClick) {
        TextButton btn = new TextButton(text, skin, "guide");
        btn.getLabel().setAlignment(Align.left);
        btn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { onClick.run(); }
        });
        body.add(btn).width(Math.min(180f, contentWidth)).height(30).left().padBottom(4).row();
    }

    private void addParagraph(String text) {
        Label label = new Label(text, skin, "guide");
        label.setWrap(true);
        label.setAlignment(Align.topLeft);
        body.add(label).width(contentWidth).left().padBottom(14).row();
    }

    private Stack iconFor(Class<? extends Component> type) {
        Stack stack = new Stack();
        Texture block = textures.getBlock(type);
        stack.add(new Image(new TextureRegionDrawable(block)));
        Texture label = textures.getLabel(type);
        if (label != null) stack.add(new Image(new TextureRegionDrawable(label)));
        return stack;
    }

    private Image portSwatch(PortType type) {
        Texture tex = textures.getPort(type, Directions.NORTH);
        return tex == null ? new Image() : new Image(new TextureRegionDrawable(tex));
    }

    public void toggle() {
        boolean show = !isVisible();
        setVisible(show);
        if (show) {
            showCategories();
        }
    }
}
