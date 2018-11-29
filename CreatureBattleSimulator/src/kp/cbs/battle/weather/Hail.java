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
public final class Hail extends Weather
{
    public Hail(int turns) { super(turns); }
    
    @Override
    public final WeatherId getId() { return WeatherId.HAIL; }
    
    @Override
    public final void start(BattleCommandManager bcm)
    {
        bcm.message("Ha empezado a granizar...").waitTime(1500);
    }
    
    @Override
    public final void end(BattleCommandManager bcm)
    {
        bcm.message("Ha dejado de granizar.").waitTime(1500);
    }

    @Override
    final void preUpdate(BattleCommandManager bcm)
    {
        bcm.message("Sigue granizando...").waitTime(1500);
    }

    @Override
    final void battleUpdate(BattleCommandManager bcm, RNG rng, Creature creature)
    {
        if(!creature.hasType(ElementalType.ICE))
            bcm.message("El granizo golpea a " + creature.getName() + "...")
                    .waitTime(500).playSound("normal_effective")
                    .damage(creature, creature.getPercentageMaxHealthPoints(0.0625f))
                    .waitTime(500);
    }
}
