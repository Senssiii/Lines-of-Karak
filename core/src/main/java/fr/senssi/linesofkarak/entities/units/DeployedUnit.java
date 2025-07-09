package fr.senssi.linesofkarak.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import fr.senssi.linesofkarak.Main;
import fr.senssi.linesofkarak.core.sprite.ShownEntity;

public class DeployedUnit extends ShownEntity {
    private Unit unit;
    private Sprite sprite;
    private int targetX,targetY;
    public boolean movedThisTurn = false;
    public boolean isSelected = true;

    public DeployedUnit(Unit unit, Sprite sprite) {
        this.unit = unit;
        this.sprite = sprite;
    }
    // X de la grille.
    public int getX(){
        return (int) (sprite.getX() / Main.GRID_SIZE);
    }

    // Y de la grille.
    public int getY(){
        return (int) (sprite.getY() / Main.GRID_SIZE);
    }


    private void tryMove() {
        if (movedThisTurn) return;
        turnMove();
    }
    private void turnMove(){
        if (isAtObjective()) {
            return;
        }

        float directionX = targetX - getX();
        float directionY = targetY - getY();

        float norme = (float) Math.sqrt(directionX * directionX + directionY * directionY);

        int dx, dy;

        if (norme <= unit.speed) {
            dx = (int) directionX;
            dy = (int) directionY;
        } else {
            float percentX = directionX / norme;
            float percentY = directionY / norme;

            dx = (int) (percentX * unit.speed);
            dy = (int) (percentY * unit.speed);
        }

        moveGrid(dx, dy);
        movedThisTurn = true;    }

    private void moveGrid(int dx, int dy){
        int newGridX = getX() + dx;
        int newGridY = getY() + dy;

        sprite.setX(newGridX * Main.GRID_SIZE);
        sprite.setY(newGridY * Main.GRID_SIZE);
        System.out.println("Position : " + getX()+ ";" + getY() );
    }
    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY());
        if (isSelected){
            batch.draw(new Texture(Gdx.files.internal("textures/effects/selected.png")),sprite.getX(),sprite.getY());
        }
    }

    private void onNewTurn(){
        movedThisTurn = false;
    }

    private boolean isAtObjective(){
        return targetX == getX() && targetY == getY();
    }

    private void newRandomObjective(){
        targetX = MathUtils.random(getX()-5,getX()+20);
        targetY = MathUtils.random(getY()-5,getY()+20);
        System.out.println("Nouvel objectif : " + targetX+ ";" + targetY);
    }

    private long lastTurnTime = System.currentTimeMillis();
    private static final long TURN_DURATION = 2000; // 3 secondes en millisecondes

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (isAtObjective()) newRandomObjective();

        if (currentTime - lastTurnTime >= TURN_DURATION) {
            onNewTurn();
            lastTurnTime = currentTime;
        }
        tryMove();
    }

}
