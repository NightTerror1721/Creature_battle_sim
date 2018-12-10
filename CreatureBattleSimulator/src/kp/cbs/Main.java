/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.io.File;
import java.io.IOException;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.AttackEffectModel;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.race.Race;
import kp.cbs.utils.Serializer;

/**
 *
 * @author Asus
 */
public final class Main
{
    public static void main(String[] args) throws IOException
    {
        /*for(int i=1;i<=100;i++)
            System.out.println("Experience gained in level " + i + ": " + Formula.experienceGained(i, i, 210, 1.5f));*/
        
        Creature c = Creature.create("Humano Joven", 100);
        Serializer.write(new File("testCreature.udl"), Serializer.extract(c));
        
        Serializer.inject(Creature.class, Serializer.read(new File("testCreature.udl")));
        
        for(ElementalType e : ElementalType.allIterable())
            System.out.println(e);
        
        Race.getAllRaces();
        
        AttackEffectModel.getModel(0);
    }
}
