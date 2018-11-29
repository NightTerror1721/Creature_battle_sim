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
public final class Rain extends Weather
{
    public Rain(int turns) { super(turns); }
    
    @Override
    public final WeatherId getId() { return WeatherId.RAIN; }
    
    @Override
    public final void start(BattleCommandManager bcm)
    {
        bcm.message("Ha empezado a llover...").waitTime(500).playSound("raining").waitTime(1500);
    }
    
    @Override
    public final void end(BattleCommandManager bcm)
    {
        bcm.message("Ha dejado de llover.").waitTime(1500);
    }

    @Override
    final void preUpdate(BattleCommandManager bcm)
    {
        bcm.message("Sigue lloviendo...").waitTime(500).playSound("raining2").waitTime(1500);
    }

    @Override
    final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature)
    {
        
    }
}
