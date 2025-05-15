package fr.senssi.linesofkarak.entities.units.soldier;

import fr.senssi.linesofkarak.entities.units.Level;
import fr.senssi.linesofkarak.entities.units.names.NameGenerator;
import fr.senssi.linesofkarak.entities.units.soldier.trait.TraitFactory;
import fr.senssi.linesofkarak.weapon.WeaponFactory;

import java.util.HashMap;
import java.util.Random;

public class SoldierFactory {

    private static HashMap<String, Soldier> basics =  new HashMap<>();
    static {
        registerBasic(new Soldier("Policier", new Level(0),10,10,25,50,WeaponFactory.get("A-AK"), TraitFactory.get(null)));
    }

    public static void registerBasic(Soldier soldier){
        basics.put(soldier.getName(), soldier);
    }
    public static Soldier getBasic(String name){
        return basics.get(name);
    }

    public static void spawnUnits(){
        for (int i = 0; i < 10;i++) new SoldierSpawned(getRandom(),"textures/soldiers/soldier_classic_soldier.png",200+new Random().nextInt(250),200+i*60);
    }
    public static Soldier getRandom(){
        int maxHp = 150;
        Random random = new Random();
        return new Soldier(
            NameGenerator.getRandomName(),
            Level.random(),
            random.nextInt(maxHp),
            random.nextInt(100),
            random.nextInt(25,40),
            random.nextInt(100),
            WeaponFactory.getRandom(),
            TraitFactory.getRandom()
        );
    }

}
