/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.util.Arrays;
import kp.cbs.ItemId;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.Creature;
import kp.cbs.creature.CreatureClass;
import kp.cbs.creature.Growth;
import kp.cbs.creature.Nature;
import kp.cbs.creature.elements.Effectivity;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.FeatureManager;
import kp.cbs.creature.feat.StatId;

/**
 *
 * @author Asus
 */
public final class Formula
{
    private Formula() {}
    
    
    public static final int MAX_STAT_ABILITY_POINTS = 512;
    public static final int MAX_ABILITY_POINTS = MAX_STAT_ABILITY_POINTS * 6;
    
    
    private static int statBase(int level, int base, int gen)
    {
        return (base * 2 + gen) * level / 100;
    }
    
    private static int statAbilityPoints(int level, int ab)
    {
        //return ab * level / 400;
        return ab * level * 160 / ((128 + ab) * 100);
    }
    
    private static int natureModification(StatId stat, Nature nature, int points)
    {
        return (int) (points * nature.getStatModificator(stat));
    }
    
    private static int normalStatModif(int levels, int points)
    {
        if(levels > 0)
            return points * (Math.min(6, levels) + 2) / 2;
        else if(levels < 0)
            return points * 2 / (Math.min(6, -levels) + 2);
        return points;
    }
    
    public static float percentageFeatModif(int levels)
    {
        if(levels > 0)
            return (Math.min(6f, levels) + 3f) / 3f;
        else if(levels < 0)
            return 3f / (Math.min(6f, -levels) + 3f);
        return 1f;
    }
    
    public static final int statValue(StatId stat, int level, int base, int gen, int ab, Nature nature, int modifLevels)
    {
        return normalStatModif(modifLevels, natureModification(stat, nature, 5 + statBase(level, base, gen) + statAbilityPoints(level, ab)));
    }
    
    private static int hpBase(int level, int base, int gen)
    {
        return (base * 2 + gen) * level / 5;
    }
    
    private static int hpAbilityPoints(int level, int ab)
    {
        //return ab * level / 20;
        return ab * level * 160 / ((128 + ab) * 5);
    }
    
    public static final int hpValue(int level, int base, int gen, int ab)
    {
        return 200 + hpBase(level, base, gen) + hpAbilityPoints(level, ab) + (level * 20);
    }
    
    private static int classStatValue(FeatureManager man, StatId statId, Nature nature, int level)
    {
        var stat = man.getStat(statId);
        return natureModification(statId, nature, statBase(level, stat.getBasePoints(), stat.getGeneticPoints()) +
                statAbilityPoints(level, stat.getAbilityPoints()));
    }
    
    public static final CreatureClass creatureClass(Creature creature)
    {
        return creatureClass(creatureClassStatSum(creature));
    }
    
    public static final CreatureClass creaturesClass(Creature... creatures)
    {
        if(creatures == null || creatures.length < 1)
            return CreatureClass.E;
        if(creatures.length == 1)
            return creatureClass(creatures[0]);
        
        int average = Arrays.stream(creatures).mapToInt(Formula::creatureClassStatSum).sum() / creatures.length;
        return creatureClass(average);
    }
    
    private static int creatureClassStatSum(Creature creature)
    {
        int level = creature.getLevel();
        var nature = creature.getNature();
        var man = creature.getFeaturesManager();
        return classStatValue(man, StatId.HEALTH_POINTS, nature, level) +
               classStatValue(man, StatId.ATTACK, nature, level) +
               classStatValue(man, StatId.DEFENSE, nature, level) +
               classStatValue(man, StatId.SPECIAL_ATTACK, nature, level) +
               classStatValue(man, StatId.SPECIAL_DEFENSE, nature, level) +
               classStatValue(man, StatId.SPEED, nature, level);
    }
    
    private static CreatureClass creatureClass(int sum)
    {
        if(sum <= 0)
            return CreatureClass.E;
        switch(sum / 100)
        {
            case 0: return CreatureClass.E;
            case 1: return CreatureClass.E2;
            case 2: return CreatureClass.E3;
            case 3: return CreatureClass.D;
            case 4: return CreatureClass.D2;
            case 5: return CreatureClass.D3;
            case 6: return CreatureClass.C;
            case 7: return CreatureClass.C2;
            case 8: return CreatureClass.C3;
            case 9: return CreatureClass.B;
            case 10: return CreatureClass.B2;
            case 11: return CreatureClass.B3;
            case 12: return CreatureClass.A;
            case 13: return CreatureClass.A2;
            case 14: return CreatureClass.A3;
            case 15: return CreatureClass.S;
            case 16: return CreatureClass.S2;
            case 17: return CreatureClass.S3;
            case 18: return CreatureClass.SS;
            case 19: return CreatureClass.SS2;
            case 20: return CreatureClass.SS3;
            default: return CreatureClass.SSS;
        }
    }
    
    
    
    public static final int expToLevel(Growth growth, int level)
    {
        level = Utils.range(2, 100, level);
        switch(growth)
        {
            case NORMAL: return level * level * level;
            case QUICK: return (int) (level * level * level * 0.8f);
            case SLOW: return (int) (level * level * level * 1.25f);
            case PARABOLIC: return (int) ((level * level * level * 1.2f) - (level * level * 15) + (level * 100) - 140);
            case ERRATIC:
                if(level <= 50) return (int) ((level * level * level) * (100 - level) / 50);
                else if(level <= 68) return (int) ((level * level * level) * (150 - level) / 100);
                else if(level <= 98) return (int) ((level * level * level) * ((150 - level) / (150 - ((level % 3) * 0.008f))));
                else return (int) ((level * level * level) * (160 - level) / 100);
            case FLUCTUATING:
                if(level <= 15) return (int) ((level * level * level) * (24 + (level - 1) / 3) / 50);
                else if(level <= 35) return (int) ((level * level * level) * (14 + level) / 50);
                else return (int) ((level * level * level) * (32 + level / 2) / 50);
            default: throw new IllegalStateException();
        }
    }
    
    public static final int experienceGained(int userLevel, int killedLevel, int base, float bonus)
    {
        int fix = base * killedLevel / 5;
        int ca = (int) Math.pow(2 * killedLevel + 10, 5 / 2);
        int cb = (int) Math.pow(killedLevel + userLevel + 10, 5 / 2);
        return (int) ((fix * ca / cb + 1) * bonus);
        //return (int) (base * killedLevel * bonus / 7);
    }
    
    public static final int creatureExpBase(int statAmount)
    {
        return (int) (statAmount * (0.2f + (statAmount / 3250f)));
    }
    
    public static final int creatureStatAbilityBase(int statAmount)
    {
        int value = Math.max(0, statAmount - 300) / 100;
        return value + 1;
    }
    
    
    public static final int baseDamage(
            RNG rng,
            int level,
            int power,
            int attack,
            int defense,
            boolean criticalHit,
            boolean burned,
            boolean stab,
            Effectivity eftype)
    {
        if(eftype.isNotEffective())
            return 0;
        
        level = Utils.range(1, 100, level);
        power = Utils.range(1, 255, power);
        attack = Utils.range(5, 9999, attack);
        defense = Utils.range(5, 9999, defense);
        
        // base
        int dam = ((level * 4 / 5) + 4) * power * attack / 5 / defense;
        
        // burned
        dam = (burned ? dam / 2 : dam) + 40;
        
        // criticalHit
        if(criticalHit)
            dam = dam * 3 / 2;
        
        // stab
        if(stab)
            dam = dam * 3 / 2;
        
        // types
        dam *= eftype.multiplier();
        
        if(dam < 1)
            dam = 1;
        
        return dam;
    }
    
    public static final boolean tryCriticalHit(RNG rng, int ratio)
    {
        switch(Utils.range(0, 5, ratio))
        {
            default: return false;
            case 1: return rng.d16(1);
            case 2: return rng.d8(1);
            case 3: return rng.d4(1);
            case 4: return rng.d2(1);
            case 5: return true;
        }
    }
    
    public static final int computeRealSpeed(Creature creature, WeatherId weather)
    {
        int value = creature.getSpeed().getValue();
        if(weather != null)
            switch(weather)
            {
                case SANDSTORM:
                    if(creature.hasAnyType(ElementalType.ROCK, ElementalType.GROUND))
                        value *= 1.5f;
                    break;
                case FOG:
                    if(creature.hasType(ElementalType.DARK))
                        value *= 1.5f;
                    break;
                case ELECTRIC_STORM:
                    if(creature.hasType(ElementalType.ELECTRIC))
                        value *= 1.5;
                    break;
            }
        if(creature.isParalyzed())
            value *= 0.25f;
        return value;
    }
    
    public static final float computePrecision(Creature user, Creature target, WeatherId weather)
    {
        float ratio = user.getAccuracy().getModification() * target.getEvasion().getModification();
        if(weather == null)
            return ratio;
        switch(weather)
        {
            case FOG:
                if(!user.hasAnyType(ElementalType.DARK, ElementalType.POISON))
                    ratio *= 0.7f;
        }
        return ratio;
    }
    
    public static final float timerCatcherMultiplier(int turn)
    {
        if(turn <= 1)
            return 1f;
        if(turn > 40)
            return 5f;
        return 1f + ((turn - 1) / 10f);
    }
    
    public static final float rapidCatcherMultiplier(int turn) { return turn == 0 ? 5f : 1f; }
    
    private static int computeCatchRatio(int statAmount)
    {
        if(statAmount <= 250)
            return 255;
        
        statAmount -= 250;
        if(statAmount >= 512)
            return 3;
        
        statAmount = (int) ((512 - statAmount) / 512f * 255f);
        return Math.max(3, statAmount);
    }
    
    private static float alterationCatchMultiplier(Creature creature)
    {
        float mul = 1f;
        for(var alt : creature.getAlterationManager().getEnabledAlterations())
        {
            switch(alt)
            {
                case PARALYSIS:
                case POISONING:
                case INTOXICATION:
                case BURN: mul += 0.5f; break;
                case SLEEP:
                case FREEZING: mul += 1f; break;
            }
        }
        return mul;
    }
    
    public static final CatchResult tryCatch(ItemId catcher, Creature target, RNG rng, int turn)
    {
        if(catcher == ItemId.MASTER_CATCHER)
            return CatchResult.CATCHED;
        var ratio = Math.min(255, computeCatchRatio(target.getFeaturesManager().getStatSum()) * catcher.getCatchMultiplier(turn));
        var maxHp = target.getMaxHealthPoints();
        var currHp = Math.max(1, target.getCurrentHealthPoints());
        var state = alterationCatchMultiplier(target);
        
        var x = (((maxHp * 3) - (currHp * 2)) * ratio / (maxHp * 3)) * state;
        int y = (int) Math.floor(65536f / Math.sqrt(Math.sqrt(255f / x)));
        
        var a = rng.d65536();
        var b = rng.d65536();
        var c = rng.d65536();
        
        return    a >= y ? (rng.d2(0) ? CatchResult.FAIL_0 : CatchResult.FAIL_1)
                : b >= y ? CatchResult.FAIL_2
                : c >= y ? CatchResult.FAIL_3
                : CatchResult.CATCHED;
    }
}
