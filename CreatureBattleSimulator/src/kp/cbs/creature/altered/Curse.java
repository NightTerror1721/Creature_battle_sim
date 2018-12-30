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
public final class Curse extends AlteredState
{
    private boolean enabled;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.CURSE; }

    @Override
    public final boolean isEnabled() { return enabled; }

    @Override
    public final void start(Creature self, RNG rng, BattleCommandManager bcm)
    {
        enabled = true;
        bcm.message(self.getName() + " ha sido maldito.")
                .waitTime(1000).playSound("effect_curse").waitTime(500);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd() && enabled)
        {
            state.bcm.message(state.self.getName() + " es víctima de una maldición...")
                    .waitTime(1000).playSound("effect_curse").damage(state.self, state.self.getPercentageMaxHealthPoints(0.25f));
        }
    }

    @Override
    public final void end(Creature self, RNG rng, BattleCommandManager bcm)
    {
        enabled = false;
        bcm.message(self.getName() + " ya no está maldito.")
                .waitTime(1000);
    }
}
