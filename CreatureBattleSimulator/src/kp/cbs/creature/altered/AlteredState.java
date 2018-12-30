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
public abstract class AlteredState
{
    public abstract AlteredStateId getId();
    
    public abstract boolean isEnabled();
    
    public abstract void start(Creature self, RNG rng, BattleCommandManager bcm);
    public abstract void update(FighterTurnState state);
    public abstract void end(Creature self, RNG rng, BattleCommandManager bcm);
}
