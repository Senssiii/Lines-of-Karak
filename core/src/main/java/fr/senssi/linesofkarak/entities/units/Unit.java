package fr.senssi.linesofkarak.entities.units;

import fr.senssi.linesofkarak.entities.Shootable;

public interface Unit extends Shootable {
    String getName();
    Level getLevel();

    float getHp();
    int getMaxHP();

    int getArmor();
    int getMaxArmor();

    int getMorale();
    int getMaxMorale();

    int getSpeed();
    int getPrecision();

    void shoot();
    boolean isDead();
    void onDeath();

    float getMoraleLossMultiplier();
    float getFlankingDamageBonus();
}
