/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import kp.cbs.creature.feat.FeatureManager;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class Creature
{
    @Property
    private FeatureManager feats;
    
    @Property
    private Experience exp;
    
    public static final Creature create(Growth growth, int level)
    {
        Creature c = new Creature();
        c.feats = new FeatureManager();
        c.exp = new Experience();
        
        c.feats.init();
        c.exp.init(Growth.NORMAL, level);
        
        return c;
    }
}
