/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.weather;

import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.creature.Creature;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public abstract class Weather
{
    private int turns;
    
    Weather(int turns)
    {
        this.turns = Math.max(1, turns);
    }
    
    public abstract WeatherId getId();
    
    public abstract void start(BattleCommandManager bcm);
    public abstract void end(BattleCommandManager bcm);
    
    public final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature c1, Creature c2)
    {
        preUpdate(bcm);
        
        if(c1.isAlive())
            battleUpdate(bcm, rng, c1);
        
        if(c2.isAlive())
            battleUpdate(bcm, rng, c2);
        turns--;
    }
    abstract void preUpdate(BattleCommandManager bcm);
    abstract void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature);
    
    public final boolean isEnd() { return turns > 0; }
}
