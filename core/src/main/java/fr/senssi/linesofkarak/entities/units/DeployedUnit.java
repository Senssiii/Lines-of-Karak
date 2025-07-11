package fr.senssi.linesofkarak.entities.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import fr.senssi.linesofkarak.Main;
import fr.senssi.linesofkarak.core.sprite.ShownEntity;
import fr.senssi.linesofkarak.map.MapManager;

import static fr.senssi.linesofkarak.map.MapManager.getCellAt;

public class DeployedUnit extends ShownEntity {
    private Unit unit;
    private Sprite sprite;
    public int targetX, targetY;
    public boolean movedThisTurn = false;
    public boolean isSelected = true;

    public DeployedUnit(Unit unit, Sprite sprite) {
        UnitManager.deployedUnits.add(this);
        this.unit = unit;
        this.sprite = sprite;
    }

    // X de la grille.
    public int getX() {
        return (int) (sprite.getX() / Main.GRID_SIZE);
    }

    // Y de la grille.
    public int getY() {
        return (int) (sprite.getY() / Main.GRID_SIZE);
    }

    private void tryMove() {
        if (movedThisTurn || isAtObjective()) return;
        turnMove();
    }

    private void turnMove() {
        if (isAtObjective()) return;

        int remainingMovement = unit.getSpeed();
        int currentX = getX();
        int currentY = getY();

        while (remainingMovement > 0 && !isAtObjective()) {
            // Calcul de la direction vers la cible
            int dirX = Integer.signum(targetX - currentX);
            int dirY = Integer.signum(targetY - currentY);

            // Essai en priorité de l'axe principal
            int moveX = 0;
            int moveY = 0;

            if (Math.abs(targetX - currentX) > Math.abs(targetY - currentY)) {
                moveX = dirX; // Priorité à l'axe X si plus éloigné
            } else {
                moveY = dirY; // Sinon priorité à l'axe Y
            }

            // Vérification du mouvement prioritaire
            int cost = getMoveCost(currentX + moveX, currentY + moveY);
            if (canMoveTo(currentX + moveX, currentY + moveY) && remainingMovement >= cost) {
                currentX += moveX;
                currentY += moveY;
                remainingMovement -= cost;
                continue;
            }

            // Si le mouvement prioritaire échoue, essai de l'autre axe
            int altMoveX = (moveX == 0) ? dirX : 0;
            int altMoveY = (moveY == 0) ? dirY : 0;
            int altCost = getMoveCost(currentX + altMoveX, currentY + altMoveY);

            if (canMoveTo(currentX + altMoveX, currentY + altMoveY) && remainingMovement >= altCost) {
                currentX += altMoveX;
                currentY += altMoveY;
                remainingMovement -= altCost;
            } else {
                break; // Aucun mouvement possible
            }
        }

        if (currentX != getX() || currentY != getY()) {
            moveGrid(currentX - getX(), currentY - getY());
            movedThisTurn = true;
        }
    }

    private int getMoveCost(int x, int y) {
        TiledMapTileLayer.Cell cell = getCellAt(Main.map, x, y);
        if (cell == null || cell.getTile() == null) {
            return Integer.MAX_VALUE;
        }

        // Vérification si la tile est traversable
        Boolean walkable = cell.getTile().getProperties().get("walkable", Boolean.class);
        if (walkable != null && !walkable) {
            return Integer.MAX_VALUE; // Mur infranchissable
        }

        // Récupération du coût directement depuis la propriété moveCost
        return cell.getTile().getProperties().get("moveCost", Integer.MAX_VALUE, Integer.class);
    }

    private boolean canMoveTo(int x, int y) {
        if (UnitManager.isThereAUnit(x, y)) {
            return false;
        }

        // Vérification de la tile de base
        TiledMapTileLayer.Cell groundCell = getCellAt(Main.map, x, y);
        if (groundCell == null || groundCell.getTile() == null) {
            return false;
        }

        // Vérification explicite de la propriété walkable
        Boolean walkable = groundCell.getTile().getProperties().get("walkable", Boolean.class);
        return walkable == null || walkable;
    }


    /**
     * Déplace l'unité à la position spécifiée
     * @param dx Le déplacement en x
     * @param dy Le déplacement en y
     */
    public void moveGrid(int dx, int dy) {
        int newGridX = getX() + dx;
        int newGridY = getY() + dy;

        sprite.setX(newGridX * Main.GRID_SIZE);
        sprite.setY(newGridY * Main.GRID_SIZE);
        System.out.println("Position : " + getX() + ";" + getY());
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY());
        if (isSelected) {
            batch.draw(new Texture(Gdx.files.internal("textures/effects/selected.png")), sprite.getX(), sprite.getY());
        }
    }

    public void onNewTurn() {
        movedThisTurn = false;
    }

    private boolean isAtObjective() {
        return targetX == getX() && targetY == getY();
    }

    private void newRandomObjective() {
        targetX = MathUtils.random(getX() - 5, getX() + 20);
        targetY = MathUtils.random(getY() - 5, getY() + 20);
        System.out.println("Nouvel objectif : " + targetX + ";" + targetY);
    }

    @Override
    public void update() {
        tryMove();
    }
}