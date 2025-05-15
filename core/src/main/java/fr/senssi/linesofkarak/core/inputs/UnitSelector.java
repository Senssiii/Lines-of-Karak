package fr.senssi.linesofkarak.core.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.senssi.linesofkarak.Main;
import fr.senssi.linesofkarak.entities.Marker;
import fr.senssi.linesofkarak.entities.units.soldier.SoldierSpawned;

import java.util.ArrayList;
import java.util.Collections;

public class UnitSelector {
    private ArrayList<SoldierSpawned> selectedUnits = new ArrayList<>();

    public void onLeftClick(float mouseX, float mouseY) {
        SoldierSpawned clickedUnit = null;

        for (SoldierSpawned unit : SoldierSpawned.units) { // On cherche sur quoi on a cliqué
            if (unit.getSprite().sprite.getBoundingRectangle().contains(mouseX, mouseY)) {
                clickedUnit = unit;
                Main.uiManager.soldierStatUI.setSoldier(clickedUnit.getUnit());

                break;
            }
        }
        if (clickedUnit == null) Main.uiManager.soldierStatUI.setSoldier(null);
        addClickedUnitToSelected(clickedUnit);
    }

    public void onRightClick(float mouseX, float mouseY) {
        // Si aucune unité n'est sélectionnée, on ne fait rien
        if (selectedUnits.isEmpty()) {
            return;
        }

        // On met un marker sur le click
        Marker marker = new Marker("X.png"); // Pour le test
        marker.sprite.setPosition(mouseX - marker.sprite.getWidth()/2f,mouseY - marker.sprite.getHeight()/2f);


        // Déplacer chaque unité sélectionnée vers la position cliquée
        Vector2 targetPosition = new Vector2(mouseX, mouseY);
        for (SoldierSpawned unit : selectedUnits) {
            if (Gdx.input.isKeyPressed(InputManager.ADD)) {
                unit.addPoint(targetPosition);
            } else {
                System.out.println("Nouvelle Fresh route");
                unit.setRoute(new ArrayList<>(Collections.singleton(targetPosition)));
                System.out.println(unit.getObjective());
            }
        }
    }

    private void unselectAll() {
        for (SoldierSpawned u : selectedUnits) {
            u.setIsSelected(false);
        }
        selectedUnits.clear();
    }

    private void addClickedUnitToSelected(SoldierSpawned clickedUnit) {
        if (clickedUnit == null) {
            unselectAll();
            return;
        }
        if (selectedUnits.isEmpty()) {
            selectedUnits.add(clickedUnit);
            clickedUnit.setIsSelected(true);
        } else if (Gdx.input.isKeyPressed(InputManager.ADD)) {
            if (!selectedUnits.contains(clickedUnit)) {
                selectedUnits.add(clickedUnit);
                clickedUnit.setIsSelected(true);
            } else {
                selectedUnits.remove(clickedUnit);
                clickedUnit.setIsSelected(false);
            }
        } else {
            for (SoldierSpawned u : selectedUnits) {
                u.setIsSelected(false);
            }
            selectedUnits.clear();
            selectedUnits.add(clickedUnit);
            clickedUnit.setIsSelected(true);
        }
    }

    public ArrayList<SoldierSpawned> getSelectedUnits() {
        return selectedUnits;
    }
}
