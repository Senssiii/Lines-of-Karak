package fr.senssi.linesofkarak.entities.units;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fr.senssi.linesofkarak.core.sprite.ShownEntity;

public class DeployedUnit extends ShownEntity {
    private Unit unit;
    private Sprite sprite;
    private Vector2 objective = new Vector2();
    public DeployedUnit(Unit unit, Sprite sprite) {
        this.unit = unit;
        this.sprite = sprite;
    }

    private boolean moveTo() {
        int gridSize = 8; // Size of each grid cell in pixels
        int objectiveX = (int) (objective.x / gridSize);
        int objectiveY = (int) (objective.y / gridSize);

        // Calculate the current grid coordinates of the soldier
        int currentX = (int) (sprite.getX() / gridSize);
        int currentY = (int) (sprite.getY() / gridSize);

        // Calculate the direction to move
        int dx = objectiveX - currentX;
        int dy = objectiveY - currentY;

        float deltaX = MathUtils.clamp(dx * gridSize, -unit.speed, unit.speed);
        float deltaY = MathUtils.clamp(dy * gridSize, -unit.speed, unit.speed);

        // Move the soldier to the next grid cell
        if (dx != 0) {
            sprite.setX(sprite.getX() + deltaX);
        }
        if (dy != 0) {
            sprite.setY(sprite.getY() + deltaY);
        }

        // Check if the soldier has reached the objective
        return currentX == objectiveX && currentY == objectiveY;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY());
    }

    private long lastObjectiveTime = System.currentTimeMillis();

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        if (moveTo() && currentTime - lastObjectiveTime > 2000) {
            lastObjectiveTime = currentTime;
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                float randomX = MathUtils.random(0, 50);
                float randomY = MathUtils.random(0, 50);
                this.objective = new Vector2(randomX + sprite.getX(), randomY + sprite.getY());
            }).start();
        }
    }
}
