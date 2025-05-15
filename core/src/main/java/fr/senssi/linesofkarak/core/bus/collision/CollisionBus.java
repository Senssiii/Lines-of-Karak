package fr.senssi.linesofkarak.core.bus.collision;

import com.badlogic.gdx.graphics.g2d.Batch;
import fr.senssi.linesofkarak.core.bus.Bus;
import fr.senssi.linesofkarak.core.sprite.Collidable;

public class CollisionBus extends Bus<Collidable> {

    @Override
    public void renderAll(Batch batch) {
        // Pas de rendu, uniquement la logique de collision
        checkCollisions();
    }

    @Override
    public void disposeAll() {
        items.clear(); // Libération simple
    }

    private void checkCollisions() {
        for (int i = 0; i < items.size(); i++) {
            Collidable a = items.get(i);
            for (int j = i + 1; j < items.size(); j++) {
                Collidable b = items.get(j);
                if (a.getBounds().overlaps(b.getBounds())) {
                    a.onCollision(items.get(j));
                    items.get(j).onCollision(a);
                }
            }
        }
    }
}
