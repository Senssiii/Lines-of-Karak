package fr.senssi.linesofkarak;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import fr.senssi.linesofkarak.core.bus.Bus;
import fr.senssi.linesofkarak.core.bus.sprite.ShownEntityBus;
import fr.senssi.linesofkarak.core.inputs.InputManager;
import fr.senssi.linesofkarak.core.sprite.ShownEntity;
import fr.senssi.linesofkarak.core.ui.SoldierStatUI;
import fr.senssi.linesofkarak.core.ui.UIManager;
import fr.senssi.linesofkarak.entities.units.DeployedUnit;
import fr.senssi.linesofkarak.entities.units.Unit;
import fr.senssi.linesofkarak.map.MapLoader;
/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private static final int VIRTUAL_WIDTH = 800;
    private static final int VIRTUAL_HEIGHT = 480;

    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private InputManager inputManager;
    public static UIManager uiManager;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        initCameraAndViewport();
        loadMap();

        ShownEntity.bus = new ShownEntityBus();

        Unit unit = new Unit("test", 1, 1, 1);
        for (int i = 0; i < 50; i++) {
            DeployedUnit deployedUnit = new DeployedUnit(unit, new Sprite(new Texture(Gdx.files.internal("orientation.png"))));
        }
        uiManager = new UIManager();
        System.out.println("ui manager initialisé");
        uiManager.soldierStatUI = new SoldierStatUI();

        uiManager.create();
        inputManager = new InputManager(camera);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputManager); // Gestion des entrées pour la caméra
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void initCameraAndViewport() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(2000, 2000, 0);
        camera.update();
    }

    private void loadMap() {
        map = MapLoader.loadMap("test_map");
        mapRenderer = new OrthogonalTiledMapRenderer(map); // Ajout du renderer
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        inputManager.handleInput();
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        uiManager.render(); // <== À faire en dehors du batch LibGDX

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Bus<?> bus : Bus.getAllBuses()) {
            bus.renderAll(batch);
        }
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiManager.resize(width,height);
    }

    @Override
    public void dispose() {
        System.out.println("Bye bye !");
        batch.dispose();
        map.dispose();
        uiManager.dispose();
        mapRenderer.dispose();
        for (Bus<?> bus : Bus.getAllBuses()) {
            bus.disposeAll();
        }
    }
}
