package fr.senssi.linesofkarak.core.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import fr.senssi.linesofkarak.core.bus.sprite.ShownEntityBus;

public abstract class ShownEntity {
    public static ShownEntityBus bus;

    /**
     * Constructeur : enregistre l'objet dans le bus global.
     */
    public ShownEntity() {
        bus.add(this);
    }

    /**
     * Dessine l'objet à l'écran avec le batch fourni.
     */
    public abstract void draw(Batch batch);

    /**
     * Met à jour l'état de l'objet.
     */
    public abstract void update();

    /**
     * Permet de retirer manuellement l'objet du SpriteBus si nécessaire.
     */
    public void removeFromBus() {
        if (bus != null) {
            bus.remove(this);
        }
    }
}
