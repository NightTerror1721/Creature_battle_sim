/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.creature.Creature;

/**
 *
 * @author Asus
 */
public final class FighterTurnState
{
    public final Creature self, enemy;
    public final BattleCommandManager bcm;
    private final int turn;
    private final boolean turnEnd;
    private boolean canAttack;
    
    public FighterTurnState(Creature self, Creature enemy, BattleCommandManager bcm, int turn, boolean turnEnd)
    {
        this.self = Objects.requireNonNull(self);
        this.enemy = Objects.requireNonNull(enemy);
        this.bcm = Objects.requireNonNull(bcm);
        this.turn = turn;
        this.turnEnd = turnEnd;
        this.canAttack = true;
    }
    
    public final int getTurn() { return turn; }
    public final boolean isTurnEnd() { return turnEnd; }
    public final boolean canAttack() { return canAttack; }
    
    public final void dissableAttack() { this.canAttack = false; }
}
