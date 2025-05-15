package fr.senssi.linesofkarak.core.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MoveableSprite extends ShownEntity {

    public Sprite sprite;

    private Vector2 lastObjective = null;
    private Float lockedAngle = null;      // angle verrouillé tant que l'objectif ne change pas
    private static final float ANGLE_EPSILON = 10f; // tolérance en degrés

    // --- CINÉMATIQUE ---
    private float angle = 0f;          // orientation actuelle en degrés
    private float maxTurnSpeed = 100f; // °/sec, à ajuster suivant ton gameplay

    public MoveableSprite(String textureName, int x, int y) {
        this.sprite = new Sprite(new Texture(textureName));
        sprite.setPosition(x, y);
        this.angle = sprite.getRotation();
    }

    /**
     * Oriente progressivement vers `objective`, en respectant `rotationSpeed` (°/s).
     */
    public void orientateTo(Vector2 objective, float rotationSpeed) {
        if (objective == null) return;

        // si nouvel objectif, on recalcul l'angle cible
        if (lastObjective == null || !lastObjective.epsilonEquals(objective, 0.5f)) {
            lastObjective = new Vector2(objective);
            lockedAngle = computeTargetAngle(objective);
        }

        if (lockedAngle != null) {
            float currentAngle = angle;
            float delta = Gdx.graphics.getDeltaTime();
            float maxRot = rotationSpeed * delta;

            // différence d'angle dans [-180,180]
            float angleDiff = wrapAngleDeg(lockedAngle - currentAngle);

            if (Math.abs(angleDiff) <= maxRot) {
                angle = lockedAngle;
            } else {
                angle = currentAngle + Math.signum(angleDiff) * maxRot;
            }
            sprite.setRotation(angle);
        }
    }

    private float computeTargetAngle(Vector2 objective) {
        Vector2 currentPos = new Vector2(
            sprite.getX() + sprite.getWidth()  / 2f,
            sprite.getY() + sprite.getHeight() / 2f
        );
        Vector2 dir = objective.cpy().sub(currentPos);
        if (dir.len() < ANGLE_EPSILON) {
            return sprite.getRotation();
        }
        return dir.angleDeg();
    }

    public void resetObjective() {
        lastObjective = null;
        lockedAngle   = null;
    }

    /**
     * Déplace en appliquant la cinématique « voiture » :
     * - tourne selon maxTurnSpeed
     * - avance dans la direction courante
     * @return true si l'objectif est atteint
     */
    public boolean moveTo(Vector2 objective, float speed) {
        Vector2 currentPos = new Vector2(
            sprite.getX() + sprite.getWidth()  / 2f,
            sprite.getY() + sprite.getHeight() / 2f
        );

        Vector2 toTarget = objective.cpy().sub(currentPos);
        float distance = toTarget.len();
        if (distance < 1e-3f) {
            sprite.setCenter(objective.x, objective.y);
            return true;
        }

        float delta = Gdx.graphics.getDeltaTime();

        float desiredAngle = toTarget.angleDeg();
        float deltaAngle = wrapAngleDeg(desiredAngle - angle);

        float maxTurn = maxTurnSpeed * delta;
        deltaAngle = MathUtils.clamp(deltaAngle, -maxTurn, maxTurn);

        angle += deltaAngle;
        sprite.setRotation(angle);

        float step = speed * delta;
        if (distance <= step) {
            sprite.setCenter(objective.x, objective.y);
            return true;
        } else {
            float dx = MathUtils.cosDeg(angle) * step;
            float dy = MathUtils.sinDeg(angle) * step;
            sprite.setCenter(currentPos.x + dx, currentPos.y + dy);
            return false;
        }
    }

    /**
     * Normalise un angle en degrés dans [-180,180].
     */
    private float wrapAngleDeg(float angleDeg) {
        float a = (angleDeg + 180f) % 360f;
        if (a < 0) a += 360f;
        return a - 180f;
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update() {
        // aucune logique par défaut
    }
}
