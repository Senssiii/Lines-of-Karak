package fr.senssi.linesofkarak.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    public SoldierStatUI soldierStatUI;

    private final List<UI> uiList = new ArrayList<>();

    private boolean showUI = true;

    public UIManager() {
    }

    public void addUI(UI ui) {
        uiList.add(ui);
    }

    public void create() {
        for (UI ui : uiList) {
            ui.create();
        }

        InputMultiplexer multiplexer = new InputMultiplexer();
        for (UI ui : uiList) {
            multiplexer.addProcessor(ui.stage);
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void render() {
        if (!showUI) return;

        for (UI ui : uiList) {
            ui.render();
        }
    }

    public void resize(int width, int height) {
        for (UI ui : uiList) {
            ui.resize(width, height);
        }
    }

    public void dispose() {
        for (UI ui : uiList) {
            ui.dispose();
        }
    }

    public void toggleUI() {
        showUI = !showUI;
        if (!showUI) {
            for (UI ui : uiList) {
                ui.clear();
            }
        } else {
            for (UI ui : uiList) {
                ui.create();
            }

            InputMultiplexer multiplexer = new InputMultiplexer();
            for (UI ui : uiList) {
                multiplexer.addProcessor((ui).stage);
            }
            Gdx.input.setInputProcessor(multiplexer);
        }
    }
}
