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
import kp.cbs.editor.MainMenuEditor;
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
        
        /*executeGame();
        
        var encounter = new Encounter();
        var rng = new RNG();
        
        encounter.getSelfTeam().addCreature(Creature.createWild(14, 100, rng));
        encounter.getEnemyTeam().addCreature(Creature.createWild(8, 100, rng));
        
        encounter.setIntelligence(AIIntelligence.create(255));
        
        Battle.initiate(null, encounter);*/
        
        executeGame();
        
        /*PlayerGame game = new PlayerGame();
        
        var rng = new RNG();
        
        var c1 = Creature.createWild(0, 5, rng);
        
        var place = Place.load("Pueblo Khalm");
        place.startWildBattle(null, game, c1);*/
        
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
        
        MainGameInterface.start();
    }
}
