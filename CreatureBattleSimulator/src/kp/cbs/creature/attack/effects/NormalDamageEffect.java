/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import java.util.StringJoiner;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.elements.Effectivity;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.utils.Formula;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class NormalDamageEffect extends DamageEffect
{
    @Property private int powerValue;
    @Property private int attackValue;
    @Property private int defenseValue;
    @Property private int backwardOrAbsorbDamage;
    @Property private boolean special;
    @Property private boolean selfTarget;
    @Property private boolean selfElementalTypeUsed;
    @Property private boolean probCriticalHit;
    
    public final void setPowerValueCustom(int value) { this.powerValue = Utils.range(1, 255, value); }
    public final void setPowerValueDefault() { this.powerValue = 0; }
    
    public final void setAttackValueCustom(int value) { this.attackValue = Utils.range(1, 9999, value); }
    public final void setAttackValueToSelf() { this.attackValue = 0; }
    public final void setAttackValueToEnemy() { this.attackValue = -1; }
    
    public final void setDefenseValueCustom(int value) { this.defenseValue = Utils.range(1, 9999, value); }
    public final void setDefenseValueToSelf() { this.defenseValue = 0; }
    public final void setDefenseValueToEnemy() { this.defenseValue = -1; }
    
    public final void setBackwardDamageOrAbsordb(int value) { this.backwardOrAbsorbDamage = Utils.range(-256, 256, value); }
    
    public final void setSpecialEnabled(boolean enabled) { this.special = enabled; }
    
    public final void setSelfTargetEnabled(boolean enabled) { this.selfTarget = enabled; }
    
    public final void setUseSelfElementalTypeEnabled(boolean enabled) { this.selfElementalTypeUsed = enabled; }
    
    public final void setProbableCriticalHitEnabled(boolean enabled) { this.probCriticalHit = enabled; }
    
    
    public final boolean isPowerValueCustom() { return powerValue > 0; }
    public final boolean isPowerValueDefault() { return powerValue <= 0; }
    public final int getCustomPowerValue() { return powerValue; }
    
    public final boolean isAttackValueCustom() { return attackValue > 0; }
    public final boolean isAttackValueToSelf() { return attackValue == 0; }
    public final boolean isAttackValueToEnemy() { return attackValue < 0; }
    public final int getCustomAttackValue() { return attackValue; }
    
    public final boolean isDefenseValueCustom() { return defenseValue > 0; }
    public final boolean isDefenseValueToSelf() { return defenseValue == 0; }
    public final boolean isDefenseValueToEnemy() { return defenseValue < 0; }
    public final int getCustomDefenseValue() { return defenseValue; }
    
    public final boolean hasBackwardDamage() { return backwardOrAbsorbDamage < 0; }
    public final boolean hasDamageAbsorbtion() { return backwardOrAbsorbDamage > 0; }
    public final int getBackwardDamageOrAbsordb() { return backwardOrAbsorbDamage; }
    
    public final boolean isSpecialEnabled() { return special; }
    
    public final boolean isSelfTargetEnabled() { return selfTarget; }
    
    public final boolean isUseSelfElementalTypeEnabled() { return selfElementalTypeUsed; }
    
    public final boolean isProbableCriticalHitEnabled() { return probCriticalHit; }
    
    private Creature self(FighterTurnState state) { return state.self; }
    private Creature enemy(FighterTurnState state) { return isSelfTargetEnabled() ? state.self : state.enemy; }
    
    private int computePowerValue(AttackModel attack, FighterTurnState state)
    {
        int pow = isPowerValueCustom()
                ? powerValue
                : attack.getPower();
        WeatherId w = state.getWeather();
        if(w != null)
        {
            ElementalType type = computeElementalType(attack, state);
            switch(w)
            {
                case RAIN:
                    if(type.equals(ElementalType.WATER))
                        pow *= 1.5f;
                    else if(type.equals(ElementalType.FIRE))
                        pow *= 0.75f;
                    break;
                case INTENSE_SUN:
                    if(type.equals(ElementalType.FIRE))
                        pow *= 1.5f;
                    else if(type.equals(ElementalType.WATER))
                        pow *= 0.75f;
                    break;
                case ELECTRIC_STORM:
                    if(type.equals(ElementalType.ELECTRIC))
                        pow *= 1.5f;
            }
        }
        return pow;
    }
    
    private int computeAttackValue(FighterTurnState state)
    {
        if(isAttackValueCustom())
            return attackValue;
        Creature creature = isAttackValueToSelf() ? self(state) : enemy(state);
        return isSpecialEnabled()
                ? creature.getSpecialAttack().getValue()
                : creature.getAttack().getValue();
    }
    
    private int computeDefenseValue(FighterTurnState state)
    {
        if(isDefenseValueCustom())
            return defenseValue;
        Creature creature = isDefenseValueToSelf() ? self(state) : enemy(state);
        return isSpecialEnabled()
                ? creature.getSpecialDefense().getValue()
                : creature.getDefense().getValue();
    }
    
    private int computeCriticalHitRatio(FighterTurnState state)
    {
        int ratio = self(state).isCriticalHitBonus() ? 2 : 1;
        if(isProbableCriticalHitEnabled())
            ratio += 2;
        if(self(state).getCurrentHealthPointsPercentage() <= 0.2f)
            ratio += 1;
        return ratio;
    }
    
    private boolean computeCriticalHit(FighterTurnState state)
    {
        return Formula.tryCriticalHit(state.rng, computeCriticalHitRatio(state));
    }
    
    private boolean computeBurned(FighterTurnState state)
    {
        return !isSpecialEnabled() && self(state).isBurned();
    }
    
    private boolean computeStab(AttackModel attack, FighterTurnState state)
    {
        return isUseSelfElementalTypeEnabled() || self(state).hasType(attack.getElementalType());
    }
    
    private ElementalType computeElementalType(AttackModel attack, FighterTurnState state)
    {
        return isUseSelfElementalTypeEnabled()
                ? self(state).getPrimaryType()
                : attack.getElementalType();
    }
    
    private Effectivity computeEffectivity(AttackModel attackModel, FighterTurnState state)
    {
        return enemy(state).effectivity(computeElementalType(attackModel, state));
    }
    
    private int computeDamage(AttackModel attackModel, FighterTurnState state, boolean criticalHit, boolean randomVariation)
    {
        Effectivity eftype = computeEffectivity(attackModel, state);
        if(eftype.isNotEffective())
            return 0;
        
        int power = computePowerValue(attackModel, state);
        int attack = computeAttackValue(state);
        int defense = computeDefenseValue(state);
        boolean burned = computeBurned(state);
        boolean stab = computeStab(attackModel, state);
        
        int dam = Formula.baseDamage(state.rng, self(state).getLevel(), power, attack, defense, criticalHit, burned, stab, eftype);
        if(randomVariation)
            return (int) (dam * ((217f + state.rng.d(40)) / 256f));
        else return (int) (dam * (217f / 256f));
    }
    
    
    
    @Override
    public final DamageEffectType getType() { return DamageEffectType.NORMAL; }
    
    @Override
    public final DamageType getDamageType() { return special ? DamageType.SPECIAL : DamageType.PHYSICAL; }

    @Override
    public final void apply(AttackModel attack, FighterTurnState state)
    {
        boolean criticalHit = computeCriticalHit(state);
        int dam = computeDamage(attack, state, criticalHit, true);
        if(dam <= 0)
        {
            state.bcm.message("¡NO AFECTA a " + enemy(state).getName() + "!");
        }
        else
        {
            Effectivity eftype = computeEffectivity(attack, state);
            state.bcm.playSound(eftype.isNotVeryEffective() ? "not_effective" : eftype.isSuperEffective() ? "super_effective" : "normal_effective")
                    .damage(enemy(state), dam);
            if(eftype.isNotVeryEffective())
                state.bcm.waitTime(500).message("No es muy efectivo...");
            else if(eftype.isSuperEffective())
                state.bcm.waitTime(500).message("¡ES SUPER EFECTIVO!");
            if(criticalHit)
                state.bcm.waitTime(1000).message("¡GOLPE CRÍTICO!");
            
            if(!isSelfTargetEnabled())
            {
                if(hasDamageAbsorbtion())
                {
                    float percentage = getBackwardDamageOrAbsordb() / 256f;
                    dam *= percentage;
                    state.bcm.waitTime(1000)
                            .playSound("heal")
                            .heal(self(state), dam)
                            .waitTime(500)
                            .message(self(state).getName() + " ha absorbido energia.");
                }
                else if(hasBackwardDamage())
                {
                    float percentage = getBackwardDamageOrAbsordb() / -256f;
                    dam *= percentage;
                    state.bcm.waitTime(1000)
                            .damage(self(state), dam)
                            .waitTime(500)
                            .message(self(state).getName() + " sufre daño por retroceso.");
                }
            }
        }
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        AIScore score = AIScore.maximum();
        
        //Base
        if(intel.isHightOrLess())
        {
            score.multiply(computePowerValue(attack, state) / 160f);
            
            //Type effectivity
            if(intel.isGreaterOrEqualsThan(AIIntelligence.MIN_NORMAL_RATIO / 4))
                score.multiply(computeEffectivity(attack, state).multiplier());
            
            //Backward damage penality
            if(intel.isNormalOrGreater())
            {
                int ratio = getBackwardDamageOrAbsordb() / -8;
                if(ratio >= 32)
                    return score.minimize();
                score.multiply((80f - ratio) / 80f);
            }
            
            //Burned penality
            if(!isSpecialEnabled() && intel.isNormalOrGreater() && self(state).isBurned())
                score.multiply(5f / 8f);
        }
        else
        {
            int dam = computeDamage(attack, state, false, false);
            if(dam <= 0)
                return score.nullify();
            else
            {
                float enemyHp = enemy(state).getCurrentHealthPoints();
                if(enemyHp > dam)
                    score.multiply(dam / enemyHp);
                
                //Backward damage penality
                {
                    float penality = (int) (dam * (getBackwardDamageOrAbsordb() / -256f));
                    if(penality >= self(state).getCurrentHealthPoints())
                        return score.minimize();
                    
                    int ratio = (int) (penality / self(state).getCurrentHealthPoints() * 32f);
                    if(ratio < 1)
                        ratio = 1;
                    score.multiply((32 - ratio) / 32f);
                }
                
                //Critical hit ratio bonus
                if(intel.isGreaterOrEqualsThan(AIIntelligence.MIN_VERY_HIGHT_RATIO + AIIntelligence.MIN_NORMAL_RATIO / 2))
                {
                    int chRatio = computeCriticalHitRatio(state);
                    if(chRatio > 1)
                        score.multiply(chRatio >= 5 ? 1.5f : ((16f + chRatio) / 16f));
                }
            }
        }
        
        //Absorbtion bonus
        if(intel.isGreaterOrEqualsThan(AIIntelligence.MIN_NORMAL_RATIO / 2))
        {
            int baseRatio = getBackwardDamageOrAbsordb() / 64;
            if(intel.isNormalOrGreater())
            {
                int hpRatio = (int) ((1f - self(state).getCurrentHealthPointsPercentage()) * 8);
                if(hpRatio > 1)
                    score.multiply((14f + baseRatio + hpRatio) / 16f);
            }
            else score.multiply((16f + baseRatio) / 16f);
        }
        
        return isSelfTargetEnabled() ? score.invert() : score;
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        StringJoiner joiner = new StringJoiner(". ", "", ".");
        
        if(isSelfTargetEnabled())
            joiner.add("Causa daño al usuario del ataque");
        
        if(isUseSelfElementalTypeEnabled())
            joiner.add("Empleará el tipo primario del usuario como tipo del ataque");
        
        if(isPowerValueCustom())
            joiner.add("Poder base concreto de ").add(Integer.toString(getCustomPowerValue()));
        
        if(isAttackValueCustom())
            joiner.add("Valor de ").add(special ? "ataque" : "ataque especial").add(" base predefinido de ").add(Integer.toString(getCustomAttackValue()));
        else if(isAttackValueToEnemy())
            joiner.add("Usa el ").add(special ? "ataque" : "ataque especial").add(" del objetivo");
        
        if(isDefenseValueCustom())
            joiner.add("Valor de ").add(special ? "defensa" : "defensa especial").add(" base predefinido de ").add(Integer.toString(getCustomDefenseValue()));
        else if(isDefenseValueToEnemy())
            joiner.add("Usa la ").add(special ? "defensa" : "defensa especial").add(" del objetivo");
        
        if(hasBackwardDamage())
            joiner.add("El usuario sufrirá daño por retroceso del ").add(bytePercentage(-getBackwardDamageOrAbsordb()));
        else if(hasDamageAbsorbtion())
            joiner.add("El usuario absorberá el ").add(bytePercentage(getBackwardDamageOrAbsordb())).add(" del daño producido");
        
        if(isProbableCriticalHitEnabled())
            joiner.add("Alta probabilidad de golpe crítico");
        
        return joiner.toString();
    }
    
    private static String bytePercentage(int value)
    {
        return ((int) (value / 256f * 100)) + "%";
    }
}
