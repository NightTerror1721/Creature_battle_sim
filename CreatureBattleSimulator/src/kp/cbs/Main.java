/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.io.File;
import java.io.IOException;
import kp.cbs.creature.Creature;
import kp.cbs.creature.Growth;
import kp.udl.UDL;

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
        
        Creature c = Creature.create(Growth.NORMAL, 100);
        UDL.write(UDL.extract(c), new File("testCreature.udl"));
        
        c = UDL.inject(UDL.read(new File("testCreature.udl")), Creature.class);
    }
}
