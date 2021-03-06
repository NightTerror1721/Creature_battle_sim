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
public final class Burn extends AlteredState
{
    private boolean enabled;
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.BURN; }

    @Override
    public final boolean isEnabled() { return enabled; }

    @Override
    public final void start(Creature self, RNG rng, BattleCommandManager bcm)
    {
        enabled = true;
        bcm.message(self.getName() + " se ha quemado.")
                .waitTime(1000).playSound("effect_flamewheel2").waitTime(500);
        self.removeAlteration(rng, bcm, AlteredStateId.FREEZING);
    }

    @Override
    public final void update(FighterTurnState state)
    {
        if(state.isTurnEnd() && enabled)
        {
            state.bcm.message(state.self.getName() + " sufre daño por quedamuras...")
                    .waitTime(1000).playSound("effect_flamewheel1").damage(state.self, state.self.getPercentageMaxHealthPoints(0.0625f));
        }
    }

    @Override
    public final void end(Creature self, RNG rng, BattleCommandManager bcm)
    {
        enabled = false;
        bcm.message(self.getName() + " ya no está quemado.")
                .waitTime(1000);
    }
    
}
