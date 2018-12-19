/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import java.util.StringJoiner;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.feat.NormalStat;
import kp.cbs.creature.feat.PercentageFeature;
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
            case 0: state.bcm.statModification(enemy(state), StatId.ATTACK, levels).message(msg(state, stat, levels)); return true;
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
    
    private float computeStatAIScore(FighterTurnState state, AIIntelligence intel, StatId modStat, StatId contraStat, int levels, float ratio)
    {
        return computeStatAIScore(this, state, intel, modStat, contraStat, levels, ratio, isSelfTargetEnabled());
    }
    
    static final float computeStatAIScore(SecondaryEffect effect,
            FighterTurnState state, AIIntelligence intel, StatId modStat, StatId contraStat, int levels, float ratio, boolean isSelfTarget)
    {
        if(levels == 0)
            return 0f;
        
        float mod = computeMod(state, intel);
        if(isSelfTarget)
        {
            NormalStat selfStat = (NormalStat) state.self.getStat(modStat);
            NormalStat targetStat = (NormalStat) state.enemy.getStat(contraStat);
            if(levels > 0)
            {
                if(intel.isNormalOrGreater() && selfStat.getAlterationLevels() >= 2)
                    return 0f;
                return targetStat.getValue() * mod / selfStat.getValue() * ratio;
            }
            else
            {
                if(intel.isLowOrLess())
                    return -ratio;
                return targetStat.getValue() * mod / selfStat.getValue() * -ratio;
            }
        }
        else
        {
            NormalStat selfStat = (NormalStat) state.self.getStat(contraStat);
            NormalStat targetStat = (NormalStat) state.enemy.getStat(modStat);
            if(levels > 0)
            {
                if(intel.isLowOrLess())
                    return -ratio;
                return targetStat.getValue() * mod / selfStat.getValue() * -ratio;
            }
            else
            {
                if(intel.isNormalOrGreater() && selfStat.getAlterationLevels() >= 2)
                    return 0f;
                return targetStat.getValue() * mod / selfStat.getValue() * ratio;
            }
        }
    }
     
    private float computePrecisionAIScore(FighterTurnState state, AIIntelligence intel, boolean isAccuracy, float ratio)
    {
        int levels = isAccuracy ? accuracyLevels : evasionLevels;
        if(levels == 0)
            return 0f;
        
        float mod = computeMod(state, intel);
        if(isSelfTargetEnabled())
        {
            PercentageFeature selfFeat = isAccuracy ? state.self.getAccuracy() : state.self.getEvasion();
            PercentageFeature targetFeat = isAccuracy ? state.enemy.getEvasion() : state.enemy.getAccuracy();
            if(levels > 0)
            {
                if(selfFeat.getAlterationLevels() - targetFeat.getAlterationLevels() < 0)
                    return ratio;
                return 0f;
            }
            else
            {
                int val = selfFeat.getAlterationLevels() - levels - targetFeat.getAlterationLevels();
                return val < 0 ? -ratio : val == 0 ? -ratio / 2f : -ratio / 4f;
            }
        }
        else
        {
            PercentageFeature selfFeat = isAccuracy ? state.self.getEvasion() : state.self.getAccuracy();
            PercentageFeature targetFeat = isAccuracy ? state.enemy.getAccuracy() : state.enemy.getEvasion();
            if(levels > 0)
            {
                int val = selfFeat.getAlterationLevels() - levels - targetFeat.getAlterationLevels();
                return val < 0 ? -ratio : val == 0 ? -ratio / 2f : -ratio / 4f;
            }
            else
            {
                if(selfFeat.getAlterationLevels() - targetFeat.getAlterationLevels() < 0)
                    return ratio;
                return 0f;
            }
        }
    }
    
    private static float computeMod(FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isGifted())
            return 1f;
        int base = intel.getRatio() * 2048 / AIIntelligence.GIFTED_RATIO;
        return (state.rng.d(base) - (base / 2f)) / 4096f;
    }
    
    static final float createRatio(SecondaryEffect effect, float base)
    {
        int prob = effect.getProbability();
        if(prob >= 100)
            return base;
        float mod = prob / 100f * 0.55f + 0.25f;
        return base * mod;
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        float normalRatio = createRatio(this, 1f);
        float speedRatio = createRatio(this, 0.85f);
        float precisionRatio = createRatio(this, 0.7f);
        float ratio = 0f;
        
        ratio += computeStatAIScore(state, intel, StatId.ATTACK, StatId.DEFENSE, attackLevels, normalRatio);
        ratio += computeStatAIScore(state, intel, StatId.DEFENSE, StatId.ATTACK, defenseLevels, normalRatio);
        ratio += computeStatAIScore(state, intel, StatId.SPECIAL_ATTACK, StatId.SPECIAL_DEFENSE, spAttackLevels, normalRatio);
        ratio += computeStatAIScore(state, intel, StatId.SPECIAL_DEFENSE, StatId.SPECIAL_ATTACK, spDefenseLevels, normalRatio);
        ratio += computeStatAIScore(state, intel, StatId.SPEED, StatId.SPEED, speedLevels, speedRatio);
        ratio += computePrecisionAIScore(state, intel, true, precisionRatio);
        ratio += computePrecisionAIScore(state, intel, false, precisionRatio);
        ratio = Utils.range(-1f, 1.25f, ratio);
        
        return AIScore.third(false).multiply(ratio);
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        StringJoiner joiner = new StringJoiner(". ", "", ".");
        msg(joiner, 0, attackLevels);
        msg(joiner, 1, defenseLevels);
        msg(joiner, 2, spAttackLevels);
        msg(joiner, 3, spDefenseLevels);
        msg(joiner, 4, speedLevels);
        msg(joiner, 5, accuracyLevels);
        msg(joiner, 6, evasionLevels);
        return joiner.toString();
    }
    
    private void msg(StringJoiner joiner, int stat, int levels)
    {
        String target = isSelfTargetEnabled() ? "al Usuario" : "al Enemigo";
        if(levels == 0)
            return;
        String base;
        switch(stat)
        {
            case 0: base = "el ataque "; break;
            case 1: base = "la defensa "; break;
            case 2: base = "el ataque especial "; break;
            case 3: base = "la defensa especial "; break;
            case 4: base = "la velocidad "; break;
            case 5: base = "la precisión "; break;
            case 6: base = "la evasión "; break;
            default: base = "<ERROR> "; break;
        }
        
        String measure;
        switch(levels)
        {
            case -6: case -5: case -4: case -3: measure = "Baja muchísimo "; break;
            case -2: measure = "Baja mucho "; break;
            case -1: measure = "Baja "; break;
            default: measure = "<SUBIDA INVALIDA>"; break;
            case 1: measure = "Sube "; break;
            case 2: measure = "Sube mucho "; break;
            case 3: case 4: case 5: case 6: measure = "Sube muchísimo "; break;
        }
        
        joiner.add(measure + base + target);
    }
}
