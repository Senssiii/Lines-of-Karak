package fr.senssi.linesofkarak.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import fr.senssi.linesofkarak.Main;

public class UI {
    public Stage stage;

    public UI(){
        Main.uiManager.addUI(this);
    }

    public void create() {

    }

     public void clear() {
        stage.clear();
    }

     public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

     public void resize(int width, int height) {

    }

     public void dispose() {
        stage.dispose();
    }
}
