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
public final class Fog extends Weather
{
    public Fog(int turns) { super(turns); }
    
    @Override
    public final WeatherId getId() { return WeatherId.FOG; }
    
    @Override
    public final void start(BattleCommandManager bcm)
    {
        bcm.message("Se ha levantado una densa niebla...").waitTime(1000);
    }
    
    @Override
    public final void end(BattleCommandManager bcm)
    {
        bcm.message("La niebla se disipó.").waitTime(1500);
    }

    @Override
    final void preUpdate(BattleCommandManager bcm)
    {
        bcm.message("Hay niebla densa...").waitTime(1000);
    }

    @Override
    final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature)
    {
        
    }
}
