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
public final class Nightmare extends AlteredState
{
    private boolean enabled;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.NIGHTMARE; }

    @Override
    public final boolean isEnabled() { return enabled; }

    @Override
    public final void start(FighterTurnState state)
    {
        if(state.self.isSleeping())
        {
            enabled = true;
            state.bcm.message(state.self.getName() + " ha empezado a sufrir pesadillas.")
                    .waitTime(1000).playSound("effect_curse").waitTime(500);
        }
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd() && enabled)
        {
            state.bcm.message(state.self.getName() + " sufre pesadillas...")
                    .waitTime(1000).playSound("effect_curse").damage(state.self, state.self.getPercentageMaxHealthPoints(0.25f));
        }
    }

    @Override
    public final void end(FighterTurnState state)
    {
        enabled = false;
        state.bcm.message(state.self.getName() + " dejó de sufrir pesadillas.")
                .waitTime(1000);
    }
}
