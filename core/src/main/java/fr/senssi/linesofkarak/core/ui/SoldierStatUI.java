package fr.senssi.linesofkarak.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SoldierStatUI extends UI {

    private final Skin skin;

    public SoldierStatUI() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    @Override
    public void create() {
        // if (soldier == null) return;

        // Table table = new Table();
        // table.top().left();
        // table.setFillParent(true);
        // table.pad(10);

        // Label title = new Label("Selected : ", skin, "default");
        // Label name = new Label("Name : " + soldier.getName(), skin);
        // Label health = new Label("HP : " + soldier.hp, skin);
        // Label defense = new Label("Armor : " + soldier.armor, skin);
        // Label speed = new Label("Speed : " + soldier.speed, skin);
        // Image image = new Image(soldier.weapon.getTexture());
        // image.setScale(3);

        // table.add(title).left().padBottom(10).row();
        // table.add(name).left().row();
        // table.add(health).left().row();
        // table.add(defense).left().row();
        // table.add(speed).left().row();
        // table.add(image).left().padTop(32).row();

        // stage.addActor(table);
    }
    // public void setSoldier(Soldier soldier) {
    //     clear();
    //     if (soldier == null) return;
    //     this.soldier = soldier;
    //     create();
    // }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }
}
