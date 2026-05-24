package io.github.NumberFactory.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraController implements InputProcessor {

    private static final float ZOOM_MIN = 0.25f;
    private static final float ZOOM_MAX = 4f;
    private static final float ZOOM_STEP = 0.1f;

    private final OrthographicCamera camera;
    private boolean panning = false;
    private int lastX;
    private int lastY;

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.MIDDLE && button != Input.Buttons.RIGHT) {
            return false;
        }
        panning = true;
        lastX = screenX;
        lastY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.MIDDLE && button != Input.Buttons.RIGHT) {
            return false;
        }
        panning = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!panning) {
            return false;
        }
        int dx = screenX - lastX;
        int dy = screenY - lastY;
        lastX = screenX;
        lastY = screenY;
        camera.position.x -= dx * camera.zoom;
        camera.position.y += dy * camera.zoom;
        camera.update();
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (!isCtrlDown()) {
            return false;
        }
        camera.zoom = clamp(camera.zoom + amountY * ZOOM_STEP, ZOOM_MIN, ZOOM_MAX);
        camera.update();
        return true;
    }

    private boolean isCtrlDown() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    private static float clamp(float v, float lo, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    @Override public boolean keyDown(int keycode) {
        return false;
    }
    @Override public boolean keyUp(int keycode) {
        return false;
    }
    @Override public boolean keyTyped(char character){
        return false;
    }
    @Override public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override public boolean touchCancelled(int sx, int sy, int p, int b) {
        return false;
    }
}
