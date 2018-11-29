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
public final class IntenseSun extends Weather
{
    public IntenseSun(int turns) { super(turns); }
    
    @Override
    public final WeatherId getId() { return WeatherId.INTENSE_SUN; }
    
    @Override
    public final void start(BattleCommandManager bcm)
    {
        bcm.message("Ha salido un intenso sol...").waitTime(1000);
    }
    
    @Override
    public final void end(BattleCommandManager bcm)
    {
        bcm.message("Ya no hace sol.").waitTime(1500);
    }

    @Override
    final void preUpdate(BattleCommandManager bcm)
    {
        bcm.message("Hace mucho sol...").waitTime(1000);
    }

    @Override
    final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature)
    {
        
    }
}
