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
public final class Sleepiness extends AlteredState
{
    private int turns;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.SLEEPINESS; }

    @Override
    public final boolean isEnabled() { return turns > 0; }

    @Override
    public final void start(Creature self, RNG rng, BattleCommandManager bcm)
    {
        if(!self.isSleeping())
        {
            turns = 1;
            bcm.message(self.getName() + " se encuentra somnoliento.")
                    .waitTime(1000);
        }
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd())
        {
            if(turns > 0)
            {
                if(turns < 2)
                {
                    turns++;
                }
                else
                {
                    turns = 0;
                    if(!state.self.isSleeping())
                        state.self.addAlteration(state.rng, state.bcm, new Sleep());
                }
            }
        }
    }

    @Override
    public final void end(Creature self, RNG rng, BattleCommandManager bcm)
    {
        turns = 0;
    }
}
