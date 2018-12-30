/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.creature.attack.effects.AIScore;
import kp.cbs.creature.attack.effects.DamageEffect;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.race.Race;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class AttackManager implements Iterable<Attack>
{
    @Property
    private Attack att1;
    
    @Property
    private Attack att2;
    
    @Property
    private Attack att3;
    
    @Property
    private Attack att4;
    
    
    public final Attack getAttack(int index)
    {
        switch(index)
        {
            default: throw new IllegalArgumentException();
            case 0: return att1;
            case 1: return att2;
            case 2: return att3;
            case 3: return att4;
        }
    }
    
    public final void setAttack(int index, Attack attack)
    {
        switch(index)
        {
            default: throw new IllegalArgumentException();
            case 0: att1 = Objects.requireNonNull(attack); break;
            case 1: att2 = Objects.requireNonNull(attack); break;
            case 2: att3 = Objects.requireNonNull(attack); break;
            case 3: att4 = Objects.requireNonNull(attack); break;
        }
    }
    
    public final void setAttack(int index, AttackModel attackModel)
    {
        setAttack(index, AttackPool.createAttack(attackModel));
    }
    
    public final boolean containsAttack(AttackModel model)
    {
        for(var att : this)
        {
            if(att.getModel().equals(model))
                return true;
        }
        return false;
    }
    
    public final int getFirstEmptySlot()
    {
        return att1 == null ? 0
                : att2 == null ? 1
                : att3 == null ? 2
                : att4 == null ? 3
                : -1;
    }
    
    @Override
    public final Iterator<Attack> iterator()
    {
        return new Iterator<Attack>()
        {
            private int it = 0;
            @Override public final boolean hasNext() { return it < 4; }
            @Override public final Attack next()
            {
                Attack a = getAttack(it);
                toNext();
                return a;
            }
            private Iterator<Attack> toNext()
            {
                Attack a;
                while(it < 4 && (a = getAttack(it)) == null)
                    it++;
                return this;
            }
        }.toNext();
    }
    
    public final Stream<Attack> stream()
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }
    
    private SelectedAttack selectByAI(FighterTurnState state, AIIntelligence intel, AttackModel combatModel, boolean printAIs)
    {
        LinkedList<SelectedAttack> scores = new LinkedList<>();
        for(int i=0;i<4;i++)
        {
            var a = getAttack(i);
            if(a == null || !a.hasPP())
                continue;
            scores.add(new SelectedAttack(a, a.computeAIScore(state, intel), i));
        }
        
        if(scores.isEmpty())
            return null;
        
        if(printAIs)
            System.out.println(scores); 
        
        SelectedAttack combat = new SelectedAttack(AttackPool.createAttack(combatModel), combatModel.computeAIScore(state, intel), -1);
        return scores.stream()
                .reduce(combat, (s0, s1) -> s0.score.compareTo(s1.score) > 0 ? s0 : s1);
    }
    
    public final SelectedAttack selectAttackByAI(FighterTurnState state, AIIntelligence intel)
    {
        AttackModel combatModel = AttackPool.createCombatAttackModel(state.self);
        SelectedAttack att = selectByAI(state, intel, combatModel, true);
        return att == null ? null : att;
    }
    
    public final AIScore selectScoreByAI(FighterTurnState state, AIIntelligence intel)
    {
        AttackModel combatModel = AttackPool.createCombatAttackModel(state.self);
        SelectedAttack att = selectByAI(state, intel, combatModel, false);
        return att == null ? combatModel.computeAIScore(state, intel) : att.score;
    }
    
    public final void setDefaultLearnedAttacksInLevel(Race race, int level)
    {
        AttackModel[] models = race.getAttackPool().getDefaultLearnedInLevel(level);
        att1 = models[0] != null ? AttackPool.createAttack(models[0]) : null;
        att2 = models[1] != null ? AttackPool.createAttack(models[1]) : null;
        att3 = models[2] != null ? AttackPool.createAttack(models[2]) : null;
        att4 = models[3] != null ? AttackPool.createAttack(models[3]) : null;
    }
    
    public final boolean hasAttackWithDamageType(DamageEffect.DamageType type)
    {
        for(Attack a : this)
        {
            int len = a.getTurnCount();
            for(int i=0;i<len;i++)
            {
                AttackTurn turn = a.getTurn(i);
                DamageEffect eff = turn.getDamageEffect();
                if(eff != null && eff.getDamageType() == type)
                    return true;
            }
        }
        return false;
    }
    
    public final boolean hasAttackWithType(ElementalType type)
    {
        for(Attack a : this)
            if(a.getElementalType().equals(type))
                return true;
        return false;
    }
    
    
    public static final class SelectedAttack
    {
        private final Attack attack;
        private final AIScore score;
        private final int index;
        
        private SelectedAttack(Attack attack, AIScore score, int index)
        {
            this.attack = attack;
            this.score = score;
            this.index = index;
        }
        
        public final Attack getAttack() { return attack; }
        public final int getIndex() { return index; }
        public final boolean isCombat() { return index < 0 || index > 3; }
        
        @Override
        public final String toString() { return attack.getName() + ": " + score.getScore(); }
    }
}
