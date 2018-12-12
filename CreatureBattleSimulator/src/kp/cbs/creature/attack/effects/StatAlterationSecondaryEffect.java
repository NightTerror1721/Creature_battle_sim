/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.feat.StatId;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public class StatAlterationSecondaryEffect extends SecondaryEffect
{
    @Property(set = "setAttackLevels")          private int attackLevels;
    @Property(set = "setDefenseLevels")         private int defenseLevels;
    @Property(set = "setSpecialAttackLevels")   private int spAttackLevels;
    @Property(set = "setSpecialDefenseLevels")  private int spDefenseLevels;
    @Property(set = "setSpeedLevels")           private int speedLevels;
    @Property(set = "setAccuracyLevels")        private int accuracyLevels;
    @Property(set = "setEvasionLevels")         private int evasionLevels;
    @Property                                   private boolean selfTarget;
    
    
    public final void setAttackLevels(int levels) { this.attackLevels = Utils.range(-6, 6, levels); }
    public final void setDefenseLevels(int levels) { this.defenseLevels = Utils.range(-6, 6, levels); }
    public final void setSpecialAttackLevels(int levels) { this.spAttackLevels = Utils.range(-6, 6, levels); }
    public final void setSpecialDefenseLevels(int levels) { this.spDefenseLevels = Utils.range(-6, 6, levels); }
    public final void setSpeedLevels(int levels) { this.speedLevels = Utils.range(-6, 6, levels); }
    public final void setAccuracyLevels(int levels) { this.accuracyLevels = Utils.range(-6, 6, levels); }
    public final void setEvasionLevels(int levels) { this.evasionLevels = Utils.range(-6, 6, levels); }
    
    public final boolean hasAttackAlteration() { return attackLevels != 0; }
    public final int getAttackLevels() { return attackLevels; }
    
    public final boolean hasDefenseAlteration() { return defenseLevels != 0; }
    public final int getDefenseLevels() { return defenseLevels; }
    
    public final boolean hasSpecialAttackAlteration() { return spAttackLevels != 0; }
    public final int getSpecialAttackLevels() { return spAttackLevels; }
    
    public final boolean hasSpecialDefenseAlteration() { return spDefenseLevels != 0; }
    public final int getSpecialDefenseLevels() { return spDefenseLevels; }
    
    public final boolean hasSpeedAlteration() { return speedLevels != 0; }
    public final int getSpeedLevels() { return speedLevels; }
    
    public final boolean hasAccuracyAlteration() { return accuracyLevels != 0; }
    public final int getAccuracyLevels() { return accuracyLevels; }
    
    public final boolean hasEvasionAlteration() { return evasionLevels != 0; }
    public final int getEvasionLevels() { return evasionLevels; }
    
    
    public final void setSelfTargetEnabled(boolean enabled) { this.selfTarget = enabled; }
    public final boolean isSelfTargetEnabled() { return selfTarget; }
    
    
    @Override
    public final SecondaryEffectType getType() { return SecondaryEffectType.STAT_ALTERATION; }
    
    private Creature self(FighterTurnState state) { return state.self; }
    private Creature enemy(FighterTurnState state) { return isSelfTargetEnabled() ? state.self : state.enemy; }
    
    private int levels(int stat)
    {
        switch(stat)
        {
            case 0: return attackLevels;
            case 1: return defenseLevels;
            case 2: return spAttackLevels;
            case 3: return spDefenseLevels;
            case 4: return speedLevels;
            case 5: return accuracyLevels;
            case 6: return evasionLevels;
            default: return 0;
        }
    }
    
    private String msg(FighterTurnState state, int stat, int levels)
    {
        String base;
        switch(stat)
        {
            case 0: base = "El ataque de "; break;
            case 1: base = "La defensa de "; break;
            case 2: base = "El ataque especial de "; break;
            case 3: base = "La defensa especial de "; break;
            case 4: base = "La velocidad de "; break;
            case 5: base = "La precisión de "; break;
            case 6: base = "La evasión de "; break;
            default: base = "<ERROR> "; break;
        }
        
        String measure;
        switch(levels)
        {
            case -6: case -5: case -4: case -3: measure = " ha bajado muchísimo."; break;
            case -2: measure = " ha bajado mucho."; break;
            case -1: measure = " ha bajado."; break;
            default: measure = "<SUBIDA INVALIDA>"; break;
            case 1: measure = " ha subido."; break;
            case 2: measure = " ha subido mucho."; break;
            case 3: case 4: case 5: case 6: measure = " ha subido muchísimo."; break;
        }
        
        return base + enemy(state).getName() + measure;
    }
    
    private boolean applyAlteration(FighterTurnState state, int stat, boolean isIncrease)
    {
        int levels = levels(stat);
        if(isIncrease)
        {
            if(levels <= 0)
                return false;
        }
        else if(levels >= 0)
            return false;
        
        switch(stat)
        {
            case 0:
                state.bcm.statModification(enemy(state), StatId.ATTACK, levels).message(msg(state, stat, levels)); return true;
            case 1: state.bcm.statModification(enemy(state), StatId.DEFENSE, levels).message(msg(state, stat, levels)); return true;
            case 2: state.bcm.statModification(enemy(state), StatId.SPECIAL_ATTACK, levels).message(msg(state, stat, levels)); return true;
            case 3: state.bcm.statModification(enemy(state), StatId.SPECIAL_DEFENSE, levels).message(msg(state, stat, levels)); return true;
            case 4: state.bcm.statModification(enemy(state), StatId.SPEED, levels).message(msg(state, stat, levels)); return true;
            case 5: state.bcm.accuracyModification(enemy(state), levels).message(msg(state, stat, levels)); return true;
            case 6: state.bcm.evasionModification(enemy(state), levels).message(msg(state, stat, levels)); return true;
            default: return false;
        }
    }

    @Override
    public void apply(AttackModel attack, FighterTurnState state)
    {
        boolean sound = false;
        for(int i=0;i<7;i++)
            if(applyAlteration(state, i, true) && !sound)
                sound = true;
        if(sound)
            state.bcm.playSound("increase").waitTime(1500);
        
        sound = false;
        for(int i=0;i<7;i++)
            if(applyAlteration(state, i, false) && !sound)
                sound = true;
        if(sound)
            state.bcm.playSound("decrease").waitTime(1500);
    }
    
    private int aiComputeScores(FighterTurnState state, AIIntelligence intel, int stat)
    {
        switch(stat)
        {
            case 0: {
                
            }
            default: return 0;
        }
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        return null;
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        return null;
    }
    
}
