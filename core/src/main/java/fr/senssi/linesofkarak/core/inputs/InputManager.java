package fr.senssi.linesofkarak.core.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import fr.senssi.linesofkarak.Main;
import fr.senssi.linesofkarak.entities.units.DeployedUnit;
import fr.senssi.linesofkarak.entities.units.UnitManager;
import fr.senssi.linesofkarak.map.TurnManager;

public class InputManager extends InputAdapter {
    private static final int NEW_TURN = Input.Keys.SPACE;
    private OrthographicCamera camera;
    public static final float CAMERA_SPEED = 250f; // pixels/sec
    public static final int MOVE_UP = Input.Keys.UP;
    public static final int MOVE_DOWN = Input.Keys.DOWN;
    public static final int MOVE_LEFT = Input.Keys.LEFT;
    public static final int MOVE_RIGHT = Input.Keys.RIGHT;
    public static final int QUIT_KEY = Input.Keys.ESCAPE;
    public static final int ADD = Input.Keys.SHIFT_LEFT;

    private float ZOOM_SPEED = 0.1f;
    private float MIN_ZOOM = 0.3f;
    private float MAX_ZOOM = 5f;


    public InputManager(OrthographicCamera camera) {
        this.camera = camera;
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
        int xGrid = (int) (click.x / Main.GRID_SIZE);
        int yGrid = (int) (click.y / Main.GRID_SIZE);

        if (button == Input.Buttons.LEFT) {
            UnitManager.clearSelectedDeployedUnits();

            DeployedUnit selectedUnit = UnitManager.findDeployedUnit(xGrid,yGrid);
            if (selectedUnit != null){
                selectedUnit.isSelected = !selectedUnit.isSelected;
            }

            System.out.println("Click G: " + click);
        }

        if (button == Input.Buttons.RIGHT) {
            UnitManager.newObjectivesForSelected(xGrid,yGrid);
            System.out.println("Click D: " + click);
        }

        return true;
    }

    // Gestion des entrées clavier pour le déplacement de la caméra
    public void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float dx = 0f;
        float dy = 0f;

        // Déplacement caméra
        if (Gdx.input.isKeyPressed(MOVE_UP)) dy += 1f;
        if (Gdx.input.isKeyPressed(MOVE_DOWN)) dy -= 1f;
        if (Gdx.input.isKeyPressed(MOVE_RIGHT)) dx += 1f;
        if (Gdx.input.isKeyPressed(MOVE_LEFT)) dx -= 1f;

        if (Gdx.input.isKeyJustPressed(NEW_TURN)) TurnManager.newTurn();

        if (dx != 0f || dy != 0f) {
            Vector2 direction = new Vector2(dx, dy).nor(); // Normalisation pour déplacement fluide
            camera.translate(direction.x * CAMERA_SPEED * deltaTime, direction.y * CAMERA_SPEED * deltaTime, 0f);
        }

        if (Gdx.input.isKeyJustPressed(QUIT_KEY)) {
            Gdx.app.exit(); // Quitter l'application
        }
    }
}
