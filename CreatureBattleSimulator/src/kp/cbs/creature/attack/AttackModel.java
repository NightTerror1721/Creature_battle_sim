/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.effects.AIScore;
import kp.cbs.creature.attack.effects.DamageEffect;
import kp.cbs.creature.attack.effects.SecondaryEffect;
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
    
    private AttackTurn[] turns = {};
    
    
    public final int getId() { return id; }
    public final void setId(int id) { this.id = Math.max(0, id); }
    
    public final String getName() { return name; }
    public final void setName(String name) { this.name = name == null ? "" : name; }
    
    public final int getPower() { return power; }
    public final void setPower(int power) { Utils.range(0, 255, power); }
    
    public final int getMaxPP() { return maxPP; }
    public final void setMaxPP(int pp) { this.maxPP = Utils.range(1, 40, pp); }
    
    public final int getPrecision() { return precision; }
    public final void setPrecision(int precision) { this.precision = Utils.range(0, 100, precision); }
    
    public final void setTurns(int turns)
    {
        if(this.turns == null || this.turns.length < 1)
        {
            this.turns = new AttackTurn[Math.max(0, turns)];
            for(int i=0;i<this.turns.length;i++)
                this.turns[i] = new AttackTurn();
        }
        else
        {
            AttackTurn[] old = this.turns;
            this.turns = new AttackTurn[Math.max(0, turns)];
            for(int i = 0; i < this.turns.length; i++)
                this.turns[i] = i < old.length ? old[i] : new AttackTurn();
        }
    }
    public final int getTurnCount() { return turns.length; }
    public final AttackTurn getTurn(int index) { return turns[index]; }
    public final AttackTurn[] getAllTurns() { return Arrays.copyOf(turns, turns.length); }
    
    
    public final AIScore computeAIScore(FighterTurnState state)
    {
        if(turns.length < 1)
            return AIScore.zero();
        
        AIScore[] scores = new AIScore[turns.length];
        for(int i=0;i<turns.length;i++)
            scores[i] = turns[i].computeAIScore(state);
        
        return AIScore.combine(scores);
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
        private String message = "";
        
        private DamageEffect dam = DamageEffect.NO_DAMAGE;
        
        private final LinkedList<SecondaryEffect> seffects = new LinkedList<>();
        
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
        
        private AIScore computeAIScore(FighterTurnState state)
        {
            AIScore score = dam != null ? dam.computeAIScore(AttackModel.this, state) : AIScore.zero();
            for(SecondaryEffect se : seffects)
                score = AIScore.concat(score, se.computeAIScore(AttackModel.this, state));
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
