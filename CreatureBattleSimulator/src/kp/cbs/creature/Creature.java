/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.util.Objects;
import kp.cbs.creature.feat.FeatureManager;
import kp.cbs.creature.race.Race;
import kp.cbs.creature.race.RaceReference;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector")
public final class Creature
{
    @Property
    private String name = "";
    
    @Property
    private RaceReference race;
    
    @Property
    private FeatureManager feats;
    
    @Property
    private Experience exp;
    
    private Nature nature;
    
    private Creature() {}
    
    public static final Creature create(Race race, int level)
    {
        Creature c = new Creature();
        c.race = new RaceReference(race);
        c.feats = FeatureManager.create();
        c.exp = new Experience();
        
        c.exp.init(race.getGrowth(), level);
        
        return c;
    }
    public static final Creature create(String race, int level) { return create(Race.getRace(race), level); }
    
    public final void setName(String name) { this.name = Objects.requireNonNull(name); }
    public final String getName() { return name; }
    
    
    private static Creature injector()
    {
        return new Creature();
    }
}
