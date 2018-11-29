/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.weather;

import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.creature.Creature;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public final class Sandstorm extends Weather
{
    public Sandstorm(int turns) { super(turns); }
    
    @Override
    public final WeatherId getId() { return WeatherId.SANDSTORM; }
    
    @Override
    public final void start(BattleCommandManager bcm)
    {
        bcm.message("Se ha levantado una tormenta de arena...").waitTime(500).playSound("sandstorm").waitTime(1500);
    }
    
    @Override
    public final void end(BattleCommandManager bcm)
    {
        bcm.message("Se ha disipado la tormenta de arena").waitTime(1500);
    }

    @Override
    final void preUpdate(BattleCommandManager bcm)
    {
        bcm.message("La tormenta de arena arrécia...").waitTime(500).playSound("sandstorm").waitTime(1500);
    }

    @Override
    final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature)
    {
        if(!creature.hasAnyType(ElementalType.GROUND, ElementalType.ROCK, ElementalType.STEEL))
            bcm.message("La tormenta de arena zarandea a " + creature.getName() + "...")
                    .waitTime(500).playSound("normal_effective")
                    .damage(creature, creature.getPercentageMaxHealthPoints(0.0625f))
                    .waitTime(500);
    }
}
