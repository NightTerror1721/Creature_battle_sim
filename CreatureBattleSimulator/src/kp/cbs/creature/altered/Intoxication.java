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
public final class Intoxication extends AlteredState
{
    private int severity;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.INTOXICATION; }

    @Override
    public final boolean isEnabled() { return severity > 0; }

    @Override
    public final void start(FighterTurnState state)
    {
        severity = 1;
        state.bcm.message(state.self.getName() + " ha sido gravemente envenenado.")
                .waitTime(1000).playSound("poison").waitTime(500);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd() && severity > 0)
        {
            state.bcm.message(state.self.getName() + " se resiente por el veneno...")
                    .waitTime(1000).playSound("poison").damage(state.self, state.self.getPercentageMaxHealthPoints(severity / 16f));
            severity++;
        }
    }

    @Override
    public final void end(FighterTurnState state)
    {
        severity = 0;
        state.bcm.message(state.self.getName() + " ya no está envenenado.")
                .waitTime(1000);
    }
}
