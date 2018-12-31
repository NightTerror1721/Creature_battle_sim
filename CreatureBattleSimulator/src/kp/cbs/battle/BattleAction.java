/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackPool;

/**
 *
 * @author Asus
 */
public abstract class BattleAction implements Comparable<BattleAction>
{
    public static final BattleAction attackAction(Attack attack) { return new AttackAction(attack); }
    public static final BattleAction combatAction(Creature creature) { return new AttackAction(AttackPool.createAttack(AttackPool.createCombatAttackModel(creature))); }
    public static final BattleAction continueAction() { return new NoArgsAction(BattleActionType.CONTINUE); }
    public static final BattleAction changeAction() { return new NoArgsAction(BattleActionType.CHANGE); }
    public static final BattleAction catchAction() { return new NoArgsAction(BattleActionType.CATCH); }
    public static final BattleAction runAction() { return new NoArgsAction(BattleActionType.RUN); }
    
    private BattleAction() {}
    
    public abstract BattleActionType getType();
    abstract int innerComparison(BattleAction action);
    
    public final int getPriority() { return getType().priority(); }
    
    public Attack getAttack() { throw new UnsupportedOperationException(); }
    
    @Override
    public final int compareTo(BattleAction action)
    {
        var cmp = Integer.compare(getPriority(), action.getPriority());
        return cmp != 0 ? cmp : innerComparison(action);
    }
    
    
    private static final class AttackAction extends BattleAction
    {
        private final Attack attack;
        
        private AttackAction(Attack attack) { this.attack = Objects.requireNonNull(attack); }
        
        @Override
        public final Attack getAttack() { return attack; }
        
        @Override
        public final BattleActionType getType() { return BattleActionType.ATTACK; }

        @Override
        final int innerComparison(BattleAction action)
        {
            if(action.getType() != BattleActionType.ATTACK)
                return 0;
            return Integer.compare(attack.getPriority(), action.getAttack().getPriority());
        }
    }
    
    private static final class NoArgsAction extends BattleAction
    {
        private final BattleActionType type;
        
        private NoArgsAction(BattleActionType type) { this.type = Objects.requireNonNull(type); }
        
        @Override
        public final BattleActionType getType() { return type; }

        @Override
        final int innerComparison(BattleAction action) { return 0; }
    }
    
    
    public enum BattleActionType
    {
        ATTACK, CONTINUE, CHANGE, CATCH, RUN;
        
        private final int priority()
        {
            switch(this)
            {
                default: return 0;
                case CHANGE: case CATCH: return 1;
                case RUN: return 2;
            }
        }
    } 
}
