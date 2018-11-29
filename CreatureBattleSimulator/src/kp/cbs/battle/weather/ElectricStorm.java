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
public final class ElectricStorm extends Weather
{
    public ElectricStorm(int turns) { super(turns); }
    
    @Override
    public final WeatherId getId() { return WeatherId.ELECTRIC_STORM; }
    
    @Override
    public final void start(BattleCommandManager bcm)
    {
        bcm.message("Se ha levantado una tormenta eléctrica...").waitTime(500).playSound("thunder_raining").waitTime(1500);
    }
    
    @Override
    public final void end(BattleCommandManager bcm)
    {
        bcm.message("Se ha disipado la tormenta eléctrica").waitTime(1500);
    }

    @Override
    final void preUpdate(BattleCommandManager bcm)
    {
        bcm.message("Loa tormenta eléctrica acecha...").waitTime(500).playSound("thunder_raining").waitTime(1500);
    }

    @Override
    final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature)
    {
        if(!creature.hasAnyType(ElementalType.ELECTRIC, ElementalType.GROUND))
        {
            bcm.message("La tormenta eléctrica golpea a " + creature.getName() + "...")
                    .waitTime(500).playSound("normal_effective")
                    .damage(creature, creature.getPercentageMaxHealthPoints(0.0625f))
                    .waitTime(500);
            if(rng.d100(20))
                bcm.message("La tormenta eléctrica también paraliza a " + creature.getName())
                        .waitTime(500).paralyze(creature);
        }
    }
}
