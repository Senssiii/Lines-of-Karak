package fr.senssi.linesofkarak.entities.units;

public class Unit {
    String name;
    int hp;
    int armor;
    int speed;
    public Unit(String name, int hp, int armor, int speed) {
        this.name = name;
        this.hp = hp;
        this.armor = armor;
        this.speed = speed;
    }
}
