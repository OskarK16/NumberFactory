package io.github.NumberFactory.view.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.utility.TransportComponent;
import io.github.NumberFactory.utils.Constants;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.List;

public class BoardRenderer implements Disposable {

    private static final int   CELL          = Constants.TILE_SIZE;
    private static final float ITEM_OFFSET_X = 8f;
    private static final float GHOST_ALPHA   = 0.6f;

    public record Ghost(int cellX, int cellY, Class<? extends Component> type) {}

    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final TextureRegistry textures;
    private final Texture floorTile;
    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    public BoardRenderer(OrthographicCamera camera, TextureRegistry textures) {
        this.camera = camera;
        this.textures = textures;
        this.batch = new SpriteBatch();
        this.shapes = new ShapeRenderer();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.floorTile = makeFloorTile();
    }

    private static Texture makeFloorTile() {
        Pixmap pm = new Pixmap(CELL, CELL, Pixmap.Format.RGBA8888);
        pm.setColor(0.149f, 0.165f, 0.200f, 1f);
        pm.fill();
        pm.setColor(0.094f, 0.106f, 0.130f, 1f);
        pm.drawRectangle(0, 0, CELL, CELL);
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    public void render(Board board) {
        render(board, 0.5f, null);
    }

    public void render(Board board, float tickProgress, Ghost ghost) {
        batch.setProjectionMatrix(camera.combined);
        shapes.setProjectionMatrix(camera.combined);

        drawCells(board, ghost);
        drawItemValues(board, tickProgress);
    }

    private void drawCells(Board board, Ghost ghost) {
        batch.begin();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Cell cell = board.getCell(x, y);
                if (cell == null) continue;
                float bx = worldX(x);
                float by = worldY(y, board);

                if (cell.isEmpty()) {
                    batch.draw(floorTile, bx, by, CELL, CELL);
                    if (ghost != null && ghost.type() != null
                            && ghost.cellX() == x && ghost.cellY() == y) {
                        drawGhost(ghost.type(), bx, by);
                    }
                    continue;
                }

                Component c = cell.getComponent();
                Texture block = textures.getBlock(c);
                if (block != null) batch.draw(block, bx, by, CELL, CELL);

                Texture label = textures.getLabel(c);
                if (label != null) batch.draw(label, bx, by, CELL, CELL);

                for (Directions dir : Directions.values()) {
                    PortType port = c.getPort(dir);
                    Texture portTex = textures.getPort(port, dir);
                    if (portTex != null) batch.draw(portTex, bx, by, CELL, CELL);
                }

                Texture state = stateOverlayFor(cell);
                if (state != null) batch.draw(state, bx, by, CELL, CELL);
            }
        }
        batch.end();
    }

    private void drawGhost(Class<? extends Component> type, float bx, float by) {
        Texture block = textures.getBlock(type);
        Texture label = textures.getLabel(type);
        batch.setColor(1f, 1f, 1f, GHOST_ALPHA);
        if (block != null) batch.draw(block, bx, by, CELL, CELL);
        if (label != null) batch.draw(label, bx, by, CELL, CELL);
        batch.setColor(Color.WHITE);
    }

    private void drawItemValues(Board board, float tickProgress) {
        batch.begin();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Cell cell = board.getCell(x, y);
                if (cell == null || cell.isEmpty()) continue;
                List<Item> items = cell.getComponent().getHeldItems();
                if (items.isEmpty()) continue;
                float[][] positions = itemPositions(cell.getComponent(), items.size(), x, y, board, tickProgress);
                for (int i = 0; i < positions.length; i++) {
                    String text = String.valueOf(items.get(i).getValue());
                    layout.setText(font, text);
                    float ix = positions[i][0];
                    float iy = positions[i][1];
                    font.draw(batch, layout, ix - layout.width * 0.5f, iy + layout.height * 0.5f);
                }
            }
        }
        batch.end();
    }

    private float[][] itemPositions(Component c, int count, int x, int y, Board board, float t) {
        float cx = worldX(x) + CELL * 0.5f;
        float cy = worldY(y, board) + CELL * 0.5f;

        if (c instanceof TransportComponent tc) {
            Directions inA  = findPort(c, PortType.INPUT_A);
            Directions outA = findPort(c, PortType.OUTPUT_A);
            Directions inB  = findPort(c, PortType.INPUT_B);
            Directions outB = findPort(c, PortType.OUTPUT_B);

            List<float[]> result = new java.util.ArrayList<>(2);
            if (tc.getSlotA() != null) {
                boolean willMove = canMoveTo(board, x, y, outA);
                result.add(willMove ? interpolatedPosition(cx, cy, inA, outA, t)
                                    : staticPosition(cx, cy, inA));
            }
            if (tc.getSlotB() != null) {
                Directions dst = (outB != null) ? outB : outA;
                boolean willMove;
                if (outB != null) {
                    willMove = canMoveTo(board, x, y, dst);
                } else {
                    willMove = tc.getSlotA() == null && canMoveTo(board, x, y, dst);
                }
                result.add(willMove ? interpolatedPosition(cx, cy, inB, dst, t)
                                    : staticPosition(cx, cy, inB));
            }
            if (!result.isEmpty()) return result.toArray(new float[0][]);
        }

        float[][] result = new float[count][];
        for (int i = 0; i < count; i++) {
            result[i] = new float[]{ cx + (i - (count - 1) * 0.5f) * ITEM_OFFSET_X, cy };
        }
        return result;
    }

    private static Directions findPort(Component c, PortType type) {
        for (Directions dir : Directions.values()) {
            if (c.getPort(dir) == type) return dir;
        }
        return null;
    }

    private static boolean canMoveTo(Board board, int x, int y, Directions outDir) {
        if (outDir == null) return false;
        int nx = x + outDir.dx;
        int ny = y + outDir.dy;
        if (!board.inBounds(nx, ny)) return false;
        Cell ncell = board.getCell(nx, ny);
        if (ncell == null || ncell.isEmpty()) return false;
        return ncell.getComponent().canReceive(outDir.opposite());
    }

    private static float[] staticPosition(float cx, float cy, Directions edgeDir) {
        if (edgeDir == null) return new float[]{cx, cy};
        float[] s = edgeOffset(edgeDir);
        return new float[]{ cx + s[0], cy + s[1] };
    }

    private static float[] interpolatedPosition(float cx, float cy,
                                                Directions in, Directions out, float t) {
        if (in == null || out == null) return new float[]{cx, cy};
        float[] s = edgeOffset(in);
        float[] e = edgeOffset(out);
        float dx, dy;
        if (t < 0.5f) {
            float u = t * 2f;
            dx = s[0] * (1f - u);
            dy = s[1] * (1f - u);
        } else {
            float u = (t - 0.5f) * 2f;
            dx = e[0] * u;
            dy = e[1] * u;
        }
        return new float[]{ cx + dx, cy + dy };
    }

    private static float[] edgeOffset(Directions d) {
        float half = CELL * 0.5f;
        return switch (d) {
            case NORTH -> new float[]{ 0,+half};
            case SOUTH -> new float[]{ 0,-half};
            case EAST  -> new float[]{ +half,0};
            case WEST  -> new float[]{ -half,0};
        };
    }

    private Texture stateOverlayFor(Cell cell) {
        if (cell.isInEdit())  return textures.getStateEdit();
        if (!cell.isValid())  return textures.getStateInvalid();
        return textures.getStateValid();
    }

    private static float worldX(int x)              { return x * CELL; }
    private static float worldY(int y, Board board) { return (board.height - 1 - y) * CELL; }

    @Override
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
        floorTile.dispose();
    }
}
