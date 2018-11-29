/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import kp.cbs.battle.FighterTurnState;

/**
 *
 * @author Asus
 */
public final class Paralysis extends AlteredState
{
    private boolean enabled;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.PARALYSIS; }

    @Override
    public final boolean isEnabled() { return enabled; }

    @Override
    public final void start(FighterTurnState state)
    {
        enabled = true;
        state.bcm.message(state.self.getName() + " se ha paralizado.")
                .waitTime(1000).playSound("effect_thundershock2").waitTime(500);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(!state.isTurnEnd())
        {
            if(enabled && state.rng.d100(30))
            {
                state.dissableAttack();
                state.bcm.message(state.self.getName() + " está paralizado y no se puede mover.")
                        .waitTime(1000).playSound("effect_thundershock2").waitTime(500);
            }
        }
    }

    @Override
    public final void end(FighterTurnState state)
    {
        enabled = false;
        state.bcm.message(state.self.getName() + " ya no está paralizado.")
                .waitTime(1000);
    }
    
}
