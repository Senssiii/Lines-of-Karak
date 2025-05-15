package fr.senssi.linesofkarak.entities.units.group;

import fr.senssi.linesofkarak.entities.units.Level;
import fr.senssi.linesofkarak.entities.units.Unit;
import fr.senssi.linesofkarak.entities.units.soldier.SoldierFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupFactory {
    public Group generateSquad(String name){
        List<Unit> soldiers = new ArrayList<>();
        GroupType squad = GroupTypeFactory.squad;

        int size = new Random().nextInt(squad.minSize,squad.maxSize);

        // On fait tout les nouveaux soldats
        for( int i = 0; i < size;i++) soldiers.add(SoldierFactory.getRandom());

        return new Group(name,Level.random(),soldiers,squad);
    }
}
