package fr.senssi.linesofkarak.entities.units;

public class Unit {
    String name;
    public int hp;
    public int armor;
    public int speed;

    /**
     * @param name Le nom de l'unité
     * @param hp Les points de vies
     * @param armor L'armure de l'unité
     * @param speed Le nombre de case déplacé par tour.
     */
    public Unit(String name, int hp, int armor, int speed) {
        this.name = name;
        this.hp = hp;
        this.armor = armor;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
