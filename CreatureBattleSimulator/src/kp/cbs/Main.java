/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kp.cbs.battle.Battle;
import kp.cbs.battle.Encounter;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.editor.MainMenuEditor;
import kp.cbs.utils.RNG;
import kp.cbs.utils.SoundManager;

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
        
        /*Creature c = Creature.create(0, 100);
        Serializer.write(Serializer.extract(c), "testCreature.udl");
        
        Serializer.inject(Serializer.read("testCreature.udl"), Creature.class);
        
        for(ElementalType e : ElementalType.allIterable())
            System.out.println(e);
        
        RacePool.getAllRaces(false);*/
        
        executeGame();
        
        var encounter = new Encounter();
        var rng = new RNG();
        
        encounter.getSelfTeam().addCreature(Creature.createWild(0, 50, rng));
        encounter.getEnemyTeam().addCreature(Creature.createWild(0, 35, rng));
        
        encounter.setIntelligence(AIIntelligence.create(255));
        
        Battle.initiate(null, encounter);
        
        //executeEditor();
    }
    
    private static void executeEditor()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace(System.err);
        }
        
        EventQueue.invokeLater(MainMenuEditor::start);
    }
    
    private static void executeGame()
    {
        SoundManager.loadSounds(true);
        
        try
        {
            for(var info : UIManager.getInstalledLookAndFeels())
            {
                if("CDE/Motif".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
}
