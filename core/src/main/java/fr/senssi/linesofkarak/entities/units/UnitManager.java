package fr.senssi.linesofkarak.entities.units;

import java.util.ArrayList;
import java.util.List;

public class UnitManager {
    public static List<Unit> units = new ArrayList<>();
    public static List<DeployedUnit> deployedUnits = new ArrayList<>();

    public static void newTurn(){
        for (DeployedUnit unit: deployedUnits){
            unit.onNewTurn();
        }
    }

    /**
     * Complexité N
     * @param xGrid Coordonnées sur la grille
     * @param yGrid Coordonnées sur la grille
     * @return null si rien n'est trouvé
     */
    public static DeployedUnit findDeployedUnit(int xGrid,int yGrid){
        for (DeployedUnit unit: deployedUnits){
            if (unit.getX() == xGrid && unit.getY() == yGrid){
                return unit;
            }
        }
        return null;
    }

    /**
     * Complexité N
     * @param xGrid Coordonnées sur la grille
     * @param yGrid Coordonnées sur la grille
     * @return S'il y a une unité ou non
     */
    public static boolean isThereAUnit(int xGrid,int yGrid){
        return findDeployedUnit(xGrid,yGrid) != null;
    }

    public static void newObjectivesForSelected(int gridX, int gridY){
        for (DeployedUnit u : deployedUnits){
            if (!u.isSelected) continue;

            u.targetX = gridX;
            u.targetY = gridY;
        }
    }

    public static void clearSelectedDeployedUnits() {
        for (DeployedUnit u : deployedUnits) {
            u.isSelected = false;
        }
    }
}
