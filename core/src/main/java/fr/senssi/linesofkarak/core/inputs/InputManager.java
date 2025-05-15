package fr.senssi.linesofkarak.core.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputManager extends InputAdapter {
    private OrthographicCamera camera;
    public static final float CAMERA_SPEED = 250f; // pixels/sec
    public static final int MOVE_UP = Input.Keys.UP;
    public static final int MOVE_DOWN = Input.Keys.DOWN;
    public static final int MOVE_LEFT = Input.Keys.LEFT;
    public static final int MOVE_RIGHT = Input.Keys.RIGHT;
    public static final int QUIT_KEY = Input.Keys.ESCAPE;
    public static final int ADD = Input.Keys.SHIFT_LEFT;

    private float ZOOM_SPEED = 0.1f;
    private float MIN_ZOOM = 0.2f;
    private float MAX_ZOOM = 5f;

    private UnitSelector selector;

    public InputManager(OrthographicCamera camera) {
        this.camera = camera;
        this.selector = new UnitSelector();
    }

    // On gère ici le zoom via la molette
    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY * ZOOM_SPEED;
        camera.zoom = MathUtils.clamp(camera.zoom, MIN_ZOOM, MAX_ZOOM);
        camera.update();
        return true;
    }

    // Gestion des entrées de la souris pour les clics
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 click = new Vector3(screenX, screenY, 0);
        camera.unproject(click);

        if (button == Input.Buttons.LEFT) {
            selector.onLeftClick(click.x, click.y);
            System.out.println("Click G: " + click);
        }

        if (button == Input.Buttons.RIGHT) {
            selector.onRightClick(click.x, click.y);
        }

        return true; // Traiter l'événement de la souris
    }

    // Gestion des entrées clavier pour le déplacement de la caméra
    public void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(MOVE_UP)) dy += 1f;
        if (Gdx.input.isKeyPressed(MOVE_DOWN)) dy -= 1f;
        if (Gdx.input.isKeyPressed(MOVE_RIGHT)) dx += 1f;
        if (Gdx.input.isKeyPressed(MOVE_LEFT)) dx -= 1f;

        if (dx != 0f || dy != 0f) {
            Vector2 direction = new Vector2(dx, dy).nor(); // Normalisation pour déplacement fluide
            camera.translate(direction.x * CAMERA_SPEED * deltaTime, direction.y * CAMERA_SPEED * deltaTime, 0f);
        }

        if (Gdx.input.isKeyJustPressed(QUIT_KEY)) {
            Gdx.app.exit(); // Quitter l'application
        }
    }

    // Cette méthode doit être appelée pour mettre à jour la caméra
    public void cameraStuff() {
        camera.update();
    }
}
