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
public final class Sleep extends AlteredState
{
    private final boolean fixedTurns;
    private int turns;
    
    public Sleep(boolean fixedTurns)
    {
        this.fixedTurns = fixedTurns;
    }
    public Sleep() { this(false); }
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.SLEEP; }

    @Override
    public final boolean isEnabled() { return turns > 0; }

    @Override
    public final void start(FighterTurnState state)
    {
        turns = fixedTurns ? 3 : state.rng.d7() + 1;
        state.bcm.message(state.self.getName() + " se ha dormido.")
                .waitTime(1000).playSound("sleep").waitTime(500);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(!state.isTurnEnd() && turns > 0)
        {
            if(--turns > 0)
            {
                state.dissableAttack();
                state.bcm.message(state.self.getName() + " está dormido.")
                        .waitTime(1000).playSound("sleep").waitTime(500);
            }
        }
    }

    @Override
    public final void end(FighterTurnState state)
    {
        turns = 0;
        state.bcm.message(state.self.getName() + " despertó.")
                .waitTime(1000);
        state.self.getAlterationManager().removeAlteredState(state, AlteredStateId.NIGHTMARE);
    }
    
}
