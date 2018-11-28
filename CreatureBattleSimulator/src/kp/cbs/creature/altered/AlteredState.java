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
public abstract class AlteredState
{
    public abstract AlteredStateId getId();
    
    public abstract boolean isEnabled();
    
    public abstract void start(FighterTurnState state);
    public abstract void update(FighterTurnState state);
    public abstract void end(FighterTurnState state);
}
