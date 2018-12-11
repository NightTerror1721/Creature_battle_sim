/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.creature.attack.effects.AIScore;
import kp.cbs.creature.attack.effects.DamageEffect;
import kp.cbs.creature.attack.effects.SecondaryEffect;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class AttackModel
{
    private int id;
    private String name;
    
    private int power;
    
    private int maxPP;
    
    private int precision;
    
    private int priority;
    
    private ElementalType type;
    
    private AttackTurn[] turns = {};
    
    
    public final int getId() { return id; }
    public final void setId(int id) { this.id = Math.max(0, id); }
    
    public final String getName() { return name; }
    public final void setName(String name) { this.name = name == null ? "" : name; }
    
    public final int getPower() { return power; }
    public final void setPower(int power) { Utils.range(0, 255, power); }
    
    public final int getMaxPP() { return maxPP; }
    public final void setMaxPP(int pp) { this.maxPP = Utils.range(1, 40, pp); }
    
    public final boolean isInfallible() { return precision < 1 || precision > 100; }
    public final int getPrecision() { return precision; }
    public final void setPrecision(int precision) { this.precision = Utils.range(0, 100, precision); }
    
    public final void setPriority(int priority) { this.priority = Utils.range(-7, 7, priority); }
    public final int getPriority() { return priority; }
    
    public final ElementalType getElementalType() { return type == null ? ElementalType.UNKNOWN : type; }
    public final void setElementalType(ElementalType type) { this.type = type == null ? ElementalType.UNKNOWN : type; }
    
    public final void setTurns(int turns)
    {
        if(this.turns == null || this.turns.length < 1)
        {
            this.turns = new AttackTurn[Math.max(0, turns)];
            for(int i=0;i<this.turns.length;i++)
                this.turns[i] = new AttackTurn(i);
        }
        else
        {
            AttackTurn[] old = this.turns;
            this.turns = new AttackTurn[Math.max(0, turns)];
            for(int i = 0; i < this.turns.length; i++)
                this.turns[i] = i < old.length ? old[i] : new AttackTurn(i);
        }
    }
    public final int getTurnCount() { return turns.length; }
    public final AttackTurn getTurn(int index) { return turns[index]; }
    public final AttackTurn[] getAllTurns() { return Arrays.copyOf(turns, turns.length); }
    
    
    public final AIScore computeAIScore(FighterTurnState state, AIIntelligence intel)
    {
        if(turns.length < 1)
            return AIScore.zero();
        
        AIScore[] scores = new AIScore[turns.length];
        for(int i=0;i<turns.length;i++)
            scores[i] = turns[i].computeAIScore(state, intel);
        
        AIScore score = AIScore.combine(scores);
        if(intel.isNormalOrGreater())
        {
            if(isInfallible())
                score.multiply(9f / 8f);
            else score.multiply((100f + precision * 2f) / 300f);
            
            if(priority > 0)
                score.multiply((71f + priority) / 64f);
            else if(priority < 0)
                score.multiply((63f + priority) / 64f);
        }
        
        return score;
    }
    
    public final String generateDescription()
    {
        switch(turns.length)
        {
            case 0: return "El ataque no tienen ningún efecto.";
            case 1: return turns[0].generateDescription();
            default: {
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<turns.length;i++)
                    sb.append("Turno ").append(i + 1).append(":\n")
                        .append(turns[i].generateDescription()).append("\n\n\n");
                return sb.toString();
            }
        }
    }
    
    
    
    public final class AttackTurn
    {
        private final int id;
        
        private String message = "";
        
        private int minHits = 1;
        private int maxHits = 1;
        
        private DamageEffect dam = DamageEffect.NO_DAMAGE;
        
        private final LinkedList<SecondaryEffect> seffects = new LinkedList<>();
        
        private AttackTurn(int id) { this.id = id; }
        
        
        public final int getTurnId() { return id; }
        
        public final String getMessage() { return message; }
        public final void setMessage(String message) { this.message = message == null ? "" : message; }
        
        public final void setMaxHits(int hits) { this.maxHits = Utils.range(minHits, 16, hits); }
        public final int getMaxHits() { return maxHits; }
        
        public final void setMinHits(int hits) { this.minHits = Utils.range(1, maxHits, hits); }
        public final int getMinHits() { return minHits; }
        
        public final DamageEffect getDamageEffect() { return dam == null ? DamageEffect.NO_DAMAGE : dam; }
        public final void setDamageEffect(DamageEffect dam) { this.dam = dam == null ? DamageEffect.NO_DAMAGE : dam; }
        
        public final int getSecondaryEffectCount() { return seffects.size(); }
        public final SecondaryEffect getSecondaryEffect(int index) { return seffects.get(index); }
        public final List<SecondaryEffect> getAllSecondaryEffects() { return Collections.unmodifiableList(seffects); }
        public final Iterator<SecondaryEffect> getSecondaryEffectIterator() { return getAllSecondaryEffects().iterator(); }
        public final Iterable<SecondaryEffect> getSecondaryEffectIterable() { return this::getSecondaryEffectIterator; }
        public final void setSecondaryEffects(Collection<SecondaryEffect> seffects) { this.seffects.clear(); this.seffects.addAll(seffects); }
        public final void addSecondaryEffect(SecondaryEffect effect) { this.seffects.add(effect); }
        public final void clearSecondaryEffects() { this.seffects.clear(); }
        
        
        public final void apply(FighterTurnState state)
        {
            if(message != null && !message.isEmpty())
                state.bcm.message(parseMessage(state, message));
            
            if(dam != null)
                dam.apply(AttackModel.this, state);
            
            seffects.stream()
                    .filter(se -> se.getProbability() < 1 || state.rng.d100(se.getProbability()))
                    .forEachOrdered(se -> se.apply(AttackModel.this, state));
        }
        
        private AIScore computeAIScore(FighterTurnState state, AIIntelligence intel)
        {
            AIScore score = dam != null ? dam.computeAIScore(AttackModel.this, AttackTurn.this, state, intel) : AIScore.zero();
            for(SecondaryEffect se : seffects)
                score = AIScore.concat(score, se.computeAIScore(AttackModel.this, AttackTurn.this, state, intel));
            return score;
        }
        
        private String generateDescription()
        {
            StringBuilder sb = new StringBuilder();
            
            if(dam != null)
                sb.append(dam.generateDescription(AttackModel.this));
            
            if(!seffects.isEmpty())
            {
                if(sb.length() > 0)
                    sb.append("\n\n");
                sb.append("Efectos:");
                for(SecondaryEffect se : seffects)
                    sb.append("\n  - ").append(se.generateDescription(AttackModel.this));
            }
            
            if(sb.length() < 1)
                return "Preparación del ataque.";
            return sb.toString();
        }
    }
    
    
    private static final Pattern SELF_PATTERN = Pattern.compile("<self>");
    private static final Pattern ENEMY_PATTERN = Pattern.compile("<enemy>");
    
    private static String parseMessage(FighterTurnState state, String message)
    {
        return ENEMY_PATTERN.matcher(SELF_PATTERN.matcher(message).replaceAll(state.self.getName()))
                .replaceAll(state.enemy.getName());
    }
}
