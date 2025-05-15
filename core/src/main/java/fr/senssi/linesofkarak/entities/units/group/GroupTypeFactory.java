package fr.senssi.linesofkarak.entities.units.group;

import fr.senssi.linesofkarak.entities.units.soldier.Soldier;

public class GroupTypeFactory {
    public static GroupType squad = new GroupType("squad",3,5, Soldier.class);
    public static GroupType company = new GroupType("squad",3,5, Group.class);
    public static GroupType division = new GroupType("squad",3,5, Group.class);
}
