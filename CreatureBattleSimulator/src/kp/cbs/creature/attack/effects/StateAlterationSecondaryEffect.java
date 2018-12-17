/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import java.util.StringJoiner;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.Creature;
import kp.cbs.creature.altered.AlteredStateId;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.state.StateId;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public class StateAlterationSecondaryEffect extends SecondaryEffect
{
    @Property private boolean confusion;
    @Property private boolean paralysis;
    @Property private boolean burn;
    @Property private boolean poisoning;
    @Property private boolean intoxication;
    @Property private boolean sleep;
    @Property private boolean sleepinees;
    @Property private boolean freezing;
    @Property private boolean curse;
    @Property private boolean nightmare;
    @Property private boolean critical_hit;
    @Property private boolean intimidated;
    @Property private boolean resting;
    
    @Property private boolean selfTarget;
    
    public boolean isConfusion() {
        return confusion;
    }

    public void setConfusion(boolean confusion) {
        this.confusion = confusion;
    }

    public boolean isParalysis() {
        return paralysis;
    }

    public void setParalysis(boolean paralysis) {
        this.paralysis = paralysis;
    }

    public boolean isBurn() {
        return burn;
    }

    public void setBurn(boolean burn) {
        this.burn = burn;
    }

    public boolean isPoisoning() {
        return poisoning;
    }

    public void setPoisoning(boolean poisoning) {
        this.poisoning = poisoning;
    }

    public boolean isIntoxication() {
        return intoxication;
    }

    public void setIntoxication(boolean intoxication) {
        this.intoxication = intoxication;
    }

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public boolean isSleepinees() {
        return sleepinees;
    }

    public void setSleepinees(boolean sleepinees) {
        this.sleepinees = sleepinees;
    }

    public boolean isFreezing() {
        return freezing;
    }

    public void setFreezing(boolean freezing) {
        this.freezing = freezing;
    }

    public boolean isCurse() {
        return curse;
    }

    public void setCurse(boolean curse) {
        this.curse = curse;
    }

    public boolean isNightmare() {
        return nightmare;
    }

    public void setNightmare(boolean nightmare) {
        this.nightmare = nightmare;
    }

    public boolean isCritical_hit() {
        return critical_hit;
    }

    public void setCritical_hit(boolean critical_hit) {
        this.critical_hit = critical_hit;
    }

    public boolean isIntimidated() {
        return intimidated;
    }

    public void setIntimidated(boolean intimidated) {
        this.intimidated = intimidated;
    }

    public boolean isResting() {
        return resting;
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }
    
    public final void setSelfTargetEnabled(boolean enabled) { this.selfTarget = enabled; }
    public final boolean isSelfTargetEnabled() { return selfTarget; }
    
    @Override
    public final SecondaryEffectType getType() { return SecondaryEffectType.STATE_ALTERATION; }
    
    private Creature self(FighterTurnState state) { return state.self; }
    private Creature enemy(FighterTurnState state) { return isSelfTargetEnabled() ? state.self : state.enemy; }
    
    private void applyAlteratedState(FighterTurnState state, AlteredStateId id, boolean flag)
    {
        if(!flag)
            return;
        switch(id)
        {
            case CONFUSION: state.bcm.confuse(enemy(state)); break;
            case PARALYSIS: state.bcm.paralyze(enemy(state)); break;
            case BURN: state.bcm.burn(enemy(state)); break;
            case POISONING: state.bcm.poison(enemy(state)); break;
            case INTOXICATION: state.bcm.intoxicate(enemy(state)); break;
            case SLEEP: state.bcm.sleep(enemy(state)); break;
            case SLEEPINESS: state.bcm.sleepiness(enemy(state)); break;
            case FREEZING: state.bcm.freeze(enemy(state)); break;
            case CURSE: state.bcm.curse(enemy(state)); break;
            case NIGHTMARE: state.bcm.nightmare(enemy(state)); break;
        }
    }
    
    private void applyState(FighterTurnState state, StateId id, boolean flag)
    {
        if(!flag)
            return;
        switch(id)
        {
            case CRITICAL_HIT: state.bcm.criticalHitBonus(enemy(state), true); break;
            case INTIMIDATED: state.bcm.intimidated(enemy(state), true); break;
            case RESTING: state.bcm.resting(enemy(state), true); break;
        }
    }

    @Override
    public final void apply(AttackModel attack, FighterTurnState state)
    {
        applyAlteratedState(state, AlteredStateId.CONFUSION, confusion);
        applyAlteratedState(state, AlteredStateId.PARALYSIS, paralysis);
        applyAlteratedState(state, AlteredStateId.BURN, burn);
        applyAlteratedState(state, AlteredStateId.POISONING, poisoning);
        applyAlteratedState(state, AlteredStateId.INTOXICATION, intoxication);
        applyAlteratedState(state, AlteredStateId.SLEEP, sleep);
        applyAlteratedState(state, AlteredStateId.SLEEPINESS, sleepinees);
        applyAlteratedState(state, AlteredStateId.FREEZING, freezing);
        applyAlteratedState(state, AlteredStateId.CURSE, curse);
        applyAlteratedState(state, AlteredStateId.NIGHTMARE, nightmare);
        
        applyState(state, StateId.CRITICAL_HIT, critical_hit);
        applyState(state, StateId.INTIMIDATED, intimidated);
        applyState(state, StateId.RESTING, resting);
    }
    
    private float computeAlteredStateRatio(FighterTurnState state, AlteredStateId id, boolean flag)
    {
        if(!flag || enemy(state).isAlterationEnabled(id))
            return 0f;
        
        switch(id)
        {
            case CONFUSION: return isSelfTargetEnabled() ? -0.65f : 0.65f;
            case PARALYSIS: return isSelfTargetEnabled() ? -0.75f : 0.75f;
            case BURN: return isSelfTargetEnabled() ? -0.75f : 0.75f;
            case POISONING: return isSelfTargetEnabled() ? -0.75f : 0.75f;
            case INTOXICATION: return isSelfTargetEnabled() ? -0.85f : 0.85f;
            case SLEEP: return isSelfTargetEnabled() ? -0.75f : 0.75f;
            case SLEEPINESS: return isSelfTargetEnabled() ? -0.5f : 0.5f;
            case FREEZING: return isSelfTargetEnabled() ? -0.85f : 0.85f;
            case CURSE:
                if(isSelfTargetEnabled())
                    return 0f;
                if(self(state).getCurrentHealthPointsPercentage() > 0.5)
                    return self(state).getCurrentHealthPointsPercentage() * 1f;
                return 0f;
            case NIGHTMARE:
                if(isSelfTargetEnabled())
                    return 0f;
                if(enemy(state).isSleeping())
                    return 0.9f;
                return 0f;
            default: return 0f;
        }
    }
    
    private float computeStateRatio(FighterTurnState state, StateId id, boolean flag)
    {
        if(!flag)
            return 0f;
        
        switch(id)
        {
            case CRITICAL_HIT:
                if(!isSelfTargetEnabled() || enemy(state).isCriticalHitBonus())
                    return 0f;
                return 0.4f;
            case INTIMIDATED:
                if(isSelfTargetEnabled() || enemy(state).isIntimidated())
                    return 0f;
                return 0.35f;
            case RESTING:
                if(isSelfTargetEnabled() || enemy(state).isResting())
                    return 0f;
                return 0.45f;
            default: return 0f;
        }
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        float ratio = 0f;
        ratio += computeAlteredStateRatio(state, AlteredStateId.CONFUSION, confusion);
        ratio += computeAlteredStateRatio(state, AlteredStateId.PARALYSIS, paralysis);
        ratio += computeAlteredStateRatio(state, AlteredStateId.BURN, burn);
        ratio += computeAlteredStateRatio(state, AlteredStateId.POISONING, poisoning);
        ratio += computeAlteredStateRatio(state, AlteredStateId.INTOXICATION, intoxication);
        ratio += computeAlteredStateRatio(state, AlteredStateId.SLEEP, sleep);
        ratio += computeAlteredStateRatio(state, AlteredStateId.SLEEPINESS, sleepinees);
        ratio += computeAlteredStateRatio(state, AlteredStateId.FREEZING, freezing);
        ratio += computeAlteredStateRatio(state, AlteredStateId.CURSE, curse);
        ratio += computeAlteredStateRatio(state, AlteredStateId.NIGHTMARE, nightmare);
        ratio += computeStateRatio(state, StateId.CRITICAL_HIT, critical_hit);
        ratio += computeStateRatio(state, StateId.INTIMIDATED, intimidated);
        ratio += computeStateRatio(state, StateId.RESTING, resting);
        ratio = Utils.range(-1f, 1f, ratio);
        
        return AIScore.medium(false).multiply(ratio);
    }
    
    private String msg(AlteredStateId id) { return "Proboca " + id; }
    private String msg(StateId id)
    {
        switch(id)
        {
            case CRITICAL_HIT: return "Incrementa el índice de golpe crítico";
            case INTIMIDATED: return "Intimida al objetivo impidiendo que realice su ataque si este no ha atacado ya antes";
            case RESTING: return "El usuario necesitará descansar el siguiente turno";
            default: return "";
        }
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        return new StringJoiner(". ", "", ".")
                .add(msg(AlteredStateId.CONFUSION))
                .add(msg(AlteredStateId.PARALYSIS))
                .add(msg(AlteredStateId.BURN))
                .add(msg(AlteredStateId.POISONING))
                .add(msg(AlteredStateId.INTOXICATION))
                .add(msg(AlteredStateId.SLEEP))
                .add(msg(AlteredStateId.SLEEPINESS))
                .add(msg(AlteredStateId.FREEZING))
                .add(msg(AlteredStateId.CURSE))
                .add(msg(AlteredStateId.NIGHTMARE))
                .add(msg(StateId.CRITICAL_HIT))
                .add(msg(StateId.INTIMIDATED))
                .add(msg(StateId.RESTING))
                .toString();
    }
    
}
