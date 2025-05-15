package fr.senssi.linesofkarak.core.bus.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import fr.senssi.linesofkarak.core.bus.Bus;
import fr.senssi.linesofkarak.core.sprite.ShownEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Bus dédié à la gestion des objets DrawableClass.
 */
public class ShownEntityBus extends Bus<ShownEntity> {
    private static final List<Bus<ShownEntity>> buses = new ArrayList<>();

    public ShownEntityBus() {
        super();
    }

    /**
     * Appelle draw() sur tous les DrawableClass enregistrés.
     */
    @Override
    public void renderAll(Batch batch) {
        for (ShownEntity drawable : items) {
            drawable.draw(batch);
        }
        updateAll(); // ça devrait être la ?
    }
    /**
     * Vide la liste des objets dessinables.
     */
    @Override
    public void disposeAll() {
        items.clear();
    }

    /**
     * Appelle update() sur tous les DrawableClass enregistrés.
     */
    public void updateAll() {
        for (ShownEntity drawable : items) drawable.update();
    }
}
