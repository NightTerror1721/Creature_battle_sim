/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import kp.cbs.battle.BattleUpdater;
import kp.cbs.battle.FighterTurnState;

/**
 *
 * @author Asus
 */
public final class AlteredStateManager implements BattleUpdater
{
    private final AlteredState[] states = new AlteredState[AlteredStateId.count()];
    
    public final void addAlteredState(FighterTurnState state, AlteredState alteredState)
    {
        int id = alteredState.getId().ordinal();
        if(states[id] != null)
            return;
        states[id] = alteredState;
        alteredState.start(state);
    }
    
    public final void removeAlteredState(FighterTurnState state, AlteredStateId alteredState)
    {
        int id = alteredState.ordinal();
        if(states[id] == null)
            return;
        states[id].end(state);
        states[id] = null;
    }
    
    public final boolean isAlteredStateEnabled(AlteredStateId alteredState)
    {
        int id = alteredState.ordinal();
        return states[id] != null && states[id].isEnabled();
    }
    
    public final void clearAllAlterations()
    {
        for(int i=0;i<states.length;i++)
            states[i] = null;
    }

    @Override
    public final void battleUpdate(FighterTurnState state)
    {
        for(AlteredState alteredState : states)
        {
            if(alteredState == null)
                continue;
            if(tryRemove(state, alteredState))
            {
                alteredState.update(state);
                tryRemove(state, alteredState);
            }
        }
    }
    
    private boolean tryRemove(FighterTurnState state, AlteredState alteredState)
    {
        if(!alteredState.isEnabled())
        {
            alteredState.end(state);
            states[alteredState.getId().ordinal()] = null;
            return false;
        }
        return true;
    }
}
