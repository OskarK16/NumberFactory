package io.github.NumberFactory.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.utility.GeneratorComponent;
import io.github.NumberFactory.model.components.utility.TransportComponent;
import io.github.NumberFactory.utils.CellStates;
import io.github.NumberFactory.utils.Constants;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.Map;

public class TextureRenderer implements Disposable {

    private static final float CELL = Constants.TILE_SIZE;
    private static final float ITEM_SIZE = 20f;
    private static final String DIGIT_FONT_PATH = "fonts/title.ttf";
    private static final int DIGIT_FONT_SIZE = 8;

    private final SpriteBatch batch;
    private final TextureRegistry registry;
    private final BitmapFont digitFont;
    private final GlyphLayout layout = new GlyphLayout();


    // TODO probabbly at some point we would like to extract all coloring variables in one big theme.json file
    private static final Color COLOR_PORT_A = new Color(0.165f, 0.420f, 1f, 1f);
    private static final Color COLOR_PORT_B = new Color(1f, 0.200f, 0.180f, 1f);

    private static final Color STATE_EDIT = new Color(1f, 1f, 0f, 0.7f);
    private static final Color STATE_INVALID = new Color(1f, 0f, 0f, 0.7f);
    private static final Color STATE_VALID = new Color(0f, 1f, 0f, 0.7f);
    private static final Color STATE_SELECTED = new Color(0f, 0f, 1f, 0.7f);

    public TextureRenderer(SpriteBatch batch, TextureRegistry registry) {
        this.batch = batch;
        this.registry = registry;
        this.digitFont = buildDigitFont();
    }

    private static BitmapFont buildDigitFont() {
        FileHandle file = Gdx.files.internal(DIGIT_FONT_PATH);
        if (!file.exists()) {
            BitmapFont fallback = new BitmapFont();
            fallback.setColor(Color.WHITE);
            return fallback;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = DIGIT_FONT_SIZE;
        param.color = Color.WHITE;
        param.characters = "0123456789-";
        BitmapFont font = generator.generateFont(param);
        generator.dispose();
        return font;
    }

    public void drawItem(float centerX, float centerY, int value) {
        batch.setColor(Color.WHITE);
        TextureRegion frame = registry.getItemFrame();
        if (frame != null)
            batch.draw(frame, centerX - ITEM_SIZE / 2f, centerY - ITEM_SIZE / 2f, ITEM_SIZE, ITEM_SIZE);

        drawValue(value, centerX, centerY);
    }

    private void drawValue(int value, float centerX, float centerY) {
        layout.setText(digitFont, Integer.toString(value));
        digitFont.draw(batch, layout, centerX - layout.width / 2f, centerY + layout.height / 2f);
    }

    public void drawTile(float x, float y) {
        batch.draw(registry.getFloorTile(), x, y, CELL, CELL);
    }

    public void renderCell(Cell cell, float x, float y) {
        if (cell.isEmpty()) drawTile(x, y);
        else renderComponent(cell.getComponent(), x, y, cell.getState());
    }

    public void renderComponent(Component component, float x, float y, float opacity) {
        renderComponent(component, x, y, null, opacity);
    }

    public void renderComponent(Component component, float x, float y, CellStates state) {
        renderComponent(component, x, y, state, 1f);
    }

    public void renderComponent(Component component, float x, float y, CellStates state, float opacity) {
        TextureRegion block = registry.getBlock(component);
        if (block == null) block = registry.getEmptyBlock();

        batch.setColor(1f, 1f, 1f, opacity);
        batch.draw(block, x, y, CELL, CELL);
        batch.setColor(Color.WHITE);

        TextureRegion label = registry.getLabel(component);
        if (label != null) batch.draw(label, x, y, CELL, CELL);

        boolean transport = component instanceof TransportComponent;
        for (Map.Entry<Directions, PortType> entry : component.getPorts().entrySet()) {
            drawPort(entry.getValue(), entry.getKey(), x, y, transport);
        }

        if (state != null) {
            Color stateColor = colorFor(state);
            if (stateColor != null) {
                batch.setColor(stateColor);
                batch.draw(registry.getState(), x, y, CELL, CELL);
                batch.setColor(Color.WHITE);
            }
        }

        if (component instanceof GeneratorComponent gen) {
            batch.setColor(1f, 1f, 1f, opacity);
            drawValue(gen.getSeedValue(), x + CELL / 2f, y + CELL / 2f + 4f);
            batch.setColor(Color.WHITE);
        }
    }

    public void drawPort(PortType type, Directions dir, float x, float y, boolean transport) {
        if (type == PortType.CLOSED) return;

        TextureRegion region = registry.getPort(type.isInput(), transport);
        if (region == null) return;

        Color portColor = (type == PortType.INPUT_A || type == PortType.OUTPUT_A) ? COLOR_PORT_A : COLOR_PORT_B;

        batch.setColor(portColor);
        batch.draw(region, x, y, CELL / 2f, CELL / 2f, CELL, CELL, 1f, 1f, getRotation(dir));
        batch.setColor(Color.WHITE);
    }

    private static Color colorFor(CellStates state) {
        return switch (state) {
            case EDIT -> STATE_EDIT;
            case VALID -> STATE_VALID;
            case INVALID -> STATE_INVALID;
            case SELECT -> STATE_SELECTED;
            case NULL -> null;
        };
    }

    private static float getRotation(Directions dir) {
        return switch (dir) {
            case EAST -> 0f;
            case NORTH -> 90f;
            case WEST -> 180f;
            case SOUTH -> 270f;
        };
    }

    @Override
    public void dispose() {
        digitFont.dispose();
    }
}
