/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.creature.Creature;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public final class Freezing extends AlteredState
{
    private int probs;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.FREEZING; }

    @Override
    public final boolean isEnabled() { return probs > 0; }

    @Override
    public final void start(Creature self, RNG rng, BattleCommandManager bcm)
    {
        probs = 9;
        bcm.message(self.getName() + " ha sido congelado.")
                .waitTime(1000).playSound("effect_ice1").waitTime(500);
        self.removeAlteration(rng, bcm, AlteredStateId.BURN);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd())
        {
            if(probs > 0)
            {
                if(state.rng.d10(probs))
                {
                    state.dissableAttack();
                    state.bcm.message(state.self.getName() + " está congelado y no se puede mover.")
                            .waitTime(1000).playSound("effect_ice1").waitTime(500);
                    probs--;
                }
                else probs = 0;
            }
        }
    }

    @Override
    public final void end(Creature self, RNG rng, BattleCommandManager bcm)
    {
        probs = 0;
        bcm.message(self.getName() + " ya no está congelado.")
                .waitTime(1000);
    }
}
