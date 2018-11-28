/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public final class Confusion extends AlteredState
{
    private int turns;
    
    public Confusion(RNG rng) { this.turns = rng.d5() + 1; }
    
    @Override
    public final AlteredStateId getId() { return AlteredStateId.CONFUSION; }

    @Override
    public boolean isEnabled() { return turns > 0; }

    @Override
    public void battleUpdate(FighterTurnState state)
    {
        turns--;
        if(turns <= 0)
        {
            state.bcm.message(state.self.getName() + " ya no está confuso.");
        }
        else
        {
            
        }
    }
    
}
