package fr.senssi.linesofkarak.entities.units.group;

import fr.senssi.linesofkarak.entities.units.Level;
import fr.senssi.linesofkarak.entities.units.Unit;

import java.util.List;
import java.util.Random;

public class Group implements Unit {
    private final String name;
    private List<Unit> units;
    private final GroupType type;
    private Level level;
    public float hp;
    private int maxHP;
    private int armor;
    private int maxArmor;
    private int morale;

    public Group(String name, Level level, List<Unit> units, GroupType type) {
        this.name = name;
        this.level = level;
        this.units = units;
        this.type = type;
        calculateTotals();
    }

    private void calculateTotals() {
        hp = 0;
        maxHP = 0;
        armor = 0;
        maxArmor = 0;
        morale = 0;
        for (Unit u : units) {
            hp += u.getHp();
            maxHP += u.getMaxHP();
            armor += u.getArmor();
            maxArmor += u.getMaxArmor();
            morale += u.getMorale();
        }
    }
    public boolean addUnit(Unit soldier) {
        if (type.isUnitGoodType(soldier.getClass()) &&
            type.isSizeOk(units.size())) {
            units.add(soldier);
            calculateTotals();
            return true;
        }
        return false;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public void onDeath() {
        // Implémenter si nécessaire
    }

    public boolean isRetreating() {
        return false;
    }

    public List<Unit> getUnits() {
        return units;
    }

    /**
     * Effectue un dégât au hasard sur un des membres.
     */
    @Override
    public void receiveDamage(float damage) {
        int i = new Random().nextInt(units.size());
        units.get(i).receiveDamage(damage);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public float getHp() {
        return hp;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
    public int getMaxArmor() {
        return maxArmor;
    }

    @Override
    public int getMorale() {
        return morale;
    }

    @Override
    public int getMaxMorale() {
        int sum = 0;
        for (Unit s : units) {
            sum += s.getMaxMorale();
        }
        return sum;
    }

    @Override
    public int getSpeed() {
        return units.stream().mapToInt(Unit::getSpeed).min().orElse(0);
    }

    @Override
    public int getPrecision() {
        return (int) units.stream().mapToInt(Unit::getPrecision).average().orElse(0);
    }

    @Override
    public void shoot() {
        for (Unit s : units) {
            s.shoot();
        }
    }

    @Override
    public float getMoraleLossMultiplier() {
        return (float) units.stream()
            .mapToDouble(Unit::getMoraleLossMultiplier)
            .average()
            .orElse(0);
    }

    @Override
    public float getFlankingDamageBonus() {
        return (float) units.stream()
            .mapToDouble(Unit::getFlankingDamageBonus)
            .average()
            .orElse(0);
    }
}

