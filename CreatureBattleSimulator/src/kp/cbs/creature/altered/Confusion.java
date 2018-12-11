/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.elements.Effectivity;
import kp.cbs.utils.Formula;

/**
 *
 * @author Asus
 */
public final class Confusion extends AlteredState
{
    private int turns;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.CONFUSION; }

    @Override
    public boolean isEnabled() { return turns > 0; }

    @Override
    public final void start(FighterTurnState state)
    {
        this.turns = state.rng.d5() + 1;
        state.bcm.message(state.self.getName() + " se ha confundido.")
                .waitTime(1000).playSound("confused").waitTime(500);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(!state.isTurnEnd())
        {
            turns--;
            if(turns > 0)
            {
                state.dissableAttack();
                state.bcm.message(state.self.getName() + " está confuso...")
                        .waitTime(1000).playSound("confused").waitTime(500);
                if(state.rng.d100(50))
                {
                    state.bcm.message("Tan confuso que se hiere a si mismo.")
                            .waitTime(500)
                            .playSound("normal_effective")
                            .damage(state.self, Formula.baseDamage(state.rng,
                                    state.self.getLevel(),
                                    40, //power
                                    state.self.getAttack().getValue(),
                                    state.self.getDefense().getValue(),
                                    false, //critical hit
                                    state.self.isBurned(), //is burn
                                    false, //stab
                                    Effectivity.NORMAL_EFFECTIVE));
                }
            }
        }
    }

    @Override
    public final void end(FighterTurnState state)
    {
        turns = 0;
        state.bcm.message(state.self.getName() + " ya no está confuso.")
                .waitTime(1000);
    }
    
}
