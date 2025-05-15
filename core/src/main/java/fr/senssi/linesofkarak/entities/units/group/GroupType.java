package fr.senssi.linesofkarak.entities.units.group;

import fr.senssi.linesofkarak.entities.units.Unit;

import java.util.List;

public class GroupType {

    public final String name;
    public final int minSize; // Inclusif
    public final int maxSize; // Inclusif
    private final Class<? extends Unit> type;

    public GroupType(String name,int minSize, int maxSize, Class<? extends Unit> type){
        this.name = name;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.type = type;
    }

    /**
     * Vérifie si la taille donné correspond à ce qui est autorisé (minSize,maxSize).
     */
    public boolean isSizeOk(int size){
        return (size >= minSize) && (size <= maxSize);
    }

    /**
     * Permet de savoir si la classe spécifié peut être ajouté dans ce type de groupe.
     * @param unit Le type d'unité
     * @return true : On peut ajouter, false : on peut pas
     */
    public boolean isUnitGoodType(Class<? extends Unit> unit) {
        if (unit == null) return false;

        // Vérifie si l'unité est du même type (classe exacte) que le modèle
        return unit.equals(type);
    }

}
