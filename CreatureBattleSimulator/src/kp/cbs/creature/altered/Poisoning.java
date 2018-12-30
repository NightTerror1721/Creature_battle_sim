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
public final class Poisoning extends AlteredState
{
    private boolean enabled;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.POISONING; }

    @Override
    public final boolean isEnabled() { return enabled; }

    @Override
    public final void start(Creature self, RNG rng, BattleCommandManager bcm)
    {
        enabled = true;
        bcm.message(self.getName() + " ha sido envenenado.")
                .waitTime(1000).playSound("poison").waitTime(500);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd() && enabled)
        {
            state.bcm.message(state.self.getName() + " se resiente por el veneno...")
                    .waitTime(1000).playSound("poison").damage(state.self, state.self.getPercentageMaxHealthPoints(0.125f));
        }
    }

    @Override
    public final void end(Creature self, RNG rng, BattleCommandManager bcm)
    {
        enabled = false;
        bcm.message(self.getName() + " ya no está envenenado.")
                .waitTime(1000);
    }
}
