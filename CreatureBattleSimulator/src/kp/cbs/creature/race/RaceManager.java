/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import kp.cbs.utils.Serializer;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "afterInject")
public final class RaceManager
{
    HashMap<String, Race> races;
    
    @Property(name = "races")
    LinkedList<Race> list;
    
    private RaceManager() {}
    
    private static RaceManager injector() { return new RaceManager(); }
    
    private void afterInject()
    {
        races = new HashMap<>();
        for(Race race : list)
            races.put(race.name, race);
    }
    
    
    
    /* Static operations */
    static final RaceManager RACES;
    
    static {
        RaceManager manager;
        try
        {
            manager = Serializer.inject(RaceManager.class, Serializer.read(new File("data/races.udl")));
        }
        catch(Throwable ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
            manager = null;
        }
        RACES = manager;
    }
}
