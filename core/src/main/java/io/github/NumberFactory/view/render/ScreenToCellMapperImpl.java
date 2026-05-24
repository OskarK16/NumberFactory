package io.github.NumberFactory.view.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import io.github.NumberFactory.controller.ScreenToCellMapper;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.utils.Constants;
import io.github.NumberFactory.utils.Directions;

public class ScreenToCellMapperImpl implements ScreenToCellMapper {

    private static final float PORT_BAND = 0.30f;

    private final OrthographicCamera camera;
    private final Board board;
    private final Vector3 scratch = new Vector3();

    public ScreenToCellMapperImpl(OrthographicCamera camera, Board board) {
        this.camera = camera;
        this.board = board;
    }

    @Override
    public int[] toCell(int screenX, int screenY) {
        scratch.set(screenX, screenY, 0);
        camera.unproject(scratch);
        int cx = (int) Math.floor(scratch.x / Constants.TILE_SIZE);
        int visualRow = (int) Math.floor(scratch.y / Constants.TILE_SIZE);
        int cy = board.height - 1 - visualRow;
        if (cx < 0 || cy < 0 || cx >= board.width || cy >= board.height) return null;
        return new int[]{cx, cy};
    }

    @Override
    public Directions toPortDirection(int screenX, int screenY) {
        scratch.set(screenX, screenY, 0);
        camera.unproject(scratch);
        float wx = scratch.x / Constants.TILE_SIZE;
        float wy = scratch.y / Constants.TILE_SIZE;
        float lx = wx - (float) Math.floor(wx);
        float ly = wy - (float) Math.floor(wy);

        float dN = 1f - ly;
        float dS = ly;
        float dE = 1f - lx;
        float dW = lx;

        float min = Math.min(Math.min(dN, dS), Math.min(dE, dW));
        if (min > PORT_BAND) return null;

        if (min == dN) return Directions.NORTH;
        if (min == dS) return Directions.SOUTH;
        if (min == dE) return Directions.EAST;
        return Directions.WEST;
    }
}
