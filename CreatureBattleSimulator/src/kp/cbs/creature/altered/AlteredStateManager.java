/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import java.util.StringJoiner;
import java.util.stream.Stream;
import kp.cbs.battle.BattleUpdater;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.creature.Creature;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public final class AlteredStateManager implements BattleUpdater
{
    private final AlteredState[] states = new AlteredState[AlteredStateId.count()];
    
    public final boolean addAlteredState(Creature self, RNG rng, BattleCommandManager bcm, AlteredState alteredState)
    {
        int id = alteredState.getId().ordinal();
        if(states[id] != null)
            return false;
        states[id] = alteredState;
        alteredState.start(self, rng, bcm);
        return true;
    }
    
    public final boolean removeAlteredState(Creature self, RNG rng, BattleCommandManager bcm, AlteredStateId alteredState)
    {
        int id = alteredState.ordinal();
        if(states[id] == null)
            return false;
        states[id].end(self, rng, bcm);
        states[id] = null;
        return true;
    }
    
    public final boolean rawDeleteAlterationState(AlteredStateId alteredState)
    {
        var state = states[alteredState.ordinal()] != null;
        states[alteredState.ordinal()] = null;
        return state;
    }
    public final boolean rawDeleteAllAlterationStates()
    {
        boolean state = false;
        for(int i = 0; i < states.length; i++)
        {
            if(!state && states[i] != null)
                state = true;
            states[i] = null;
        }
        return state;
    }
    
    public final boolean isAlteredStateEnabled(AlteredStateId alteredState)
    {
        int id = alteredState.ordinal();
        return states[id] != null && states[id].isEnabled();
    }
    
    public final AlteredStateId[] getEnabledAlterations()
    {
        return Stream.of(AlteredStateId.values())
                .filter(this::isAlteredStateEnabled)
                .toArray(AlteredStateId[]::new);
    }
    
    public final void clearAllAlterations()
    {
        for(int i=0;i<states.length;i++)
            states[i] = null;
    }
    
    public final String getAbbreviatedInfo()
    {
        var joiner = new StringJoiner(" ");
        for(var astate : states)
            if(astate != null)
                joiner.add(astate.getId().getAbbreviation());
        return joiner.toString();
    }
    
    public final void clearTransientAlterations()
    {
        states[AlteredStateId.CONFUSION.ordinal()] = null;
        states[AlteredStateId.CURSE.ordinal()] = null;
        states[AlteredStateId.NIGHTMARE.ordinal()] = null;
        states[AlteredStateId.SLEEPINESS.ordinal()] = null;
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
            alteredState.end(state.self, state.rng, state.bcm);
            states[alteredState.getId().ordinal()] = null;
            return false;
        }
        return true;
    }
}
