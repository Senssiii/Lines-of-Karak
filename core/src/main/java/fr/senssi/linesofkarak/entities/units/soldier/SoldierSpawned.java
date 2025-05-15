package fr.senssi.linesofkarak.entities.units.soldier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.senssi.linesofkarak.core.bus.collision.CollisionBus;
import fr.senssi.linesofkarak.core.sprite.Collidable;
import fr.senssi.linesofkarak.core.sprite.MoveableSprite;
import fr.senssi.linesofkarak.core.sprite.ShownEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SoldierSpawned extends ShownEntity implements Collidable {
    public static List<SoldierSpawned> units =  new ArrayList<>();

    private static final Sprite highlightSprite = new Sprite(
        new Texture("textures/effects/selected.png")
    );

    // Ajoute un sprite pour représenter un trait
    private static final Sprite routeSprite = new Sprite(
        new Texture("textures/effects/route.png") // <-- Mets ton image de trait ici
    );
    public static CollisionBus collisionBus;
    // -------------------
    private final Soldier soldier;
    private final MoveableSprite sprite;
    private Vector2 objective = null;
    private boolean isMoving;
    private boolean isSelected;

    private Queue<Vector2> route = new LinkedList<>();

    /**
     * Représente une unités qui a spawn dans le monde.
     * @param soldier
     * @param texture
     * @param x
     * @param y
     */
    public SoldierSpawned(Soldier soldier, String texture, int x, int y){
        this.sprite = new MoveableSprite(texture,x,y);
        this.soldier = soldier;
        isSelected = false;
        collisionBus.add(this);
        units.add(this);
    }

    public MoveableSprite getSprite() {
        return sprite;
    }

    /**
     * @param objective Position à atteindre
     */
    public boolean moveTo( Vector2 objective ){
        return sprite.moveTo(objective, soldier.getSpeed());
    }
    public void orientateTo( Vector2 objective){
        sprite.orientateTo(objective, soldier.getSpeed()*2);
    }

    /**
     * Bouge vers l'objectif définit
     */
    public void move() {
        // Si aucun objectif, passer au prochain dans la route
        if (objective == null && !route.isEmpty()) {
            objective = route.poll();
        }

        // Si on a un objectif
        if (objective != null) {
            float arrivalRadius = 10f; // tolérance de proximité pour considérer l’objectif atteint

            Vector2 currentPos = new Vector2(
                sprite.sprite.getX() + sprite.sprite.getWidth() / 2f,
                sprite.sprite.getY() + sprite.sprite.getHeight() / 2f
            );

            // 1. Vérifie si l’objectif est atteint naturellement
            if (currentPos.dst(objective) <= arrivalRadius) {
                objective = null;
                isMoving = false;
                return;
            }

            // 2. Vérifie si une autre unité est déjà sur l’objectif
            for (SoldierSpawned other : units) {
                if (other == this) continue;

                Vector2 otherPos = new Vector2(
                    other.sprite.sprite.getX() + other.sprite.sprite.getWidth() / 2f,
                    other.sprite.sprite.getY() + other.sprite.sprite.getHeight() / 2f
                );

                float otherRadius = 15f; // distance max pour dire "je touche une unité sur le point"

                if (other.objective == null && otherPos.dst(objective) <= arrivalRadius && currentPos.dst(otherPos) <= otherRadius) {
                    // On considère qu’on est collé à une unité déjà arrivée
                    objective = null;
                    isMoving = false;
                    return;
                }
            }

            // Si on n’a pas encore atteint l’objectif, continuer à s’en approcher
            isMoving = true;
            if (moveTo(objective)) {
                objective = null;
            } else {
                orientateTo(objective);
            }
        }
    }

    public Soldier getUnit() {
        return soldier;
    }

    public boolean switchIsSelected() {
        isSelected = ! isSelected;
        return isSelected;
    }
    public void setIsSelected(boolean value) {
        this.isSelected = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setRoute( ArrayList<Vector2> route ) {
        this.route = new LinkedList<>(route);
        objective = this.route.poll();
    }

    public Queue<Vector2> getRoute() {
        return route;
    }

    public void addPoint( Vector2 point){
        route.add(point);
    }

    public Vector2 getObjective() {
        return objective;
    }


    @Override
    public Rectangle getBounds() {
        return sprite.sprite.getBoundingRectangle();
    }

    @Override
    public void onCollision(Collidable other) {
        if (!(other instanceof SoldierSpawned)) return;

        SoldierSpawned otherUnit = (SoldierSpawned) other;

        Rectangle myBounds = this.getBounds();
        Rectangle otherBounds = otherUnit.getBounds();

        Vector2 myCenter = new Vector2(myBounds.x + myBounds.width / 2f, myBounds.y + myBounds.height / 2f);
        Vector2 otherCenter = new Vector2(otherBounds.x + otherBounds.width / 2f, otherBounds.y + otherBounds.height / 2f);

        // Direction de poussée : de l'autre vers soi
        Vector2 pushDir = new Vector2(myCenter).sub(otherCenter);
        float overlap = computeOverlap(myBounds, otherBounds, pushDir);

        if (overlap > 0.01f) {
            float pushFactor = 0.5f; // Chaque unité prend la moitié de la poussée

            // Normaliser la direction
            Vector2 push = pushDir.nor().scl((overlap + 0.5f) * pushFactor);

            sprite.sprite.setPosition(
                sprite.sprite.getX() + push.x,
                sprite.sprite.getY() + push.y
            );
            otherUnit.getSprite().sprite.setPosition(
                otherUnit.getSprite().sprite.getX() - push.x,
                otherUnit.getSprite().sprite.getY() - push.y
            );
        }
    }

    private float computeOverlap(Rectangle a, Rectangle b, Vector2 direction) {
        // On projette les rectangles selon l'axe de poussée
        Vector2 axis = new Vector2(direction).nor();
        float aMin = axis.dot(new Vector2(a.x, a.y));
        float aMax = axis.dot(new Vector2(a.x + a.width, a.y + a.height));
        float bMin = axis.dot(new Vector2(b.x, b.y));
        float bMax = axis.dot(new Vector2(b.x + b.width, b.y + b.height));

        return Math.min(aMax, bMax) - Math.max(aMin, bMin);
    }

    @Override
    public void draw(Batch batch) {
        if (isSelected) {
            drawHighlight(batch);
            drawRoute(batch);
        }
    }

    private void drawHighlight(Batch batch) {
        float padding = 5f;
        highlightSprite.setSize(
            sprite.sprite.getWidth() + padding,
            sprite.sprite.getHeight() + padding
        );
        highlightSprite.setOrigin(
            sprite.sprite.getOriginX() + padding / 2f,
            sprite.sprite.getOriginY() + padding / 2f
        );
        highlightSprite.setPosition(
            sprite.sprite.getX() - padding / 2f,
            sprite.sprite.getY() - padding / 2f
        );
        highlightSprite.setScale(sprite.sprite.getScaleX(), sprite.sprite.getScaleY());
        highlightSprite.setRotation(sprite.sprite.getRotation());
        highlightSprite.draw(batch);
    }

    private void drawRoute(Batch batch) {
        ArrayList<Vector2> fullRoute = new ArrayList<>();

        // Ajouter position actuelle (centre)
        fullRoute.add(new Vector2(
            sprite.sprite.getX() + sprite.sprite.getWidth() / 2f,
            sprite.sprite.getY() + sprite.sprite.getHeight() / 2f
        ));

        // Ajouter objectif actuel s’il existe
        if (objective != null) fullRoute.add(objective);

        // Ajouter le reste des points
        fullRoute.addAll(route);

        for (int i = 0; i < fullRoute.size() - 1; i++) {
            Vector2 p1 = fullRoute.get(i);
            Vector2 p2 = fullRoute.get(i + 1);

            float midX = (p1.x + p2.x) / 2f;
            float midY = (p1.y + p2.y) / 2f;
            float angle = (float) Math.toDegrees(Math.atan2(p2.y - p1.y, p2.x - p1.x));
            float distance = p1.dst(p2);

            routeSprite.setSize(distance, 5f);
            routeSprite.setOriginCenter();
            routeSprite.setRotation(angle);
            routeSprite.setPosition(midX - routeSprite.getWidth() / 2f, midY - routeSprite.getHeight() / 2f);
            routeSprite.draw(batch);
        }
    }

    @Override
    public void update() {
        move();
    }
}
