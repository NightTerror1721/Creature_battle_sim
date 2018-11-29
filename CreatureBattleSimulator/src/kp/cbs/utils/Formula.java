/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import kp.cbs.creature.Growth;
import kp.cbs.creature.Nature;
import kp.cbs.creature.elements.Effectivity;
import kp.cbs.creature.feat.StatId;

/**
 *
 * @author Asus
 */
public final class Formula
{
    private Formula() {}
    
    
    private static int statBase(int level, int base, int gen)
    {
        return (base * 2 + gen) * level / 100;
    }
    
    private static int statAbilityPoints(int level, int ab)
    {
        return ab * level / 400;
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
            return 2 / (points * Math.min(6, -levels));
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
        return ab * level / 20;
    }
    
    public static final int hpValue(int level, int base, int gen, int ab)
    {
        return 100 + hpBase(level, base, gen) + hpAbilityPoints(level, ab) + (level * 20);
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
    
    
    public static final int baseDamage(
            RNG rng,
            int level,
            int power,
            int attack,
            int defense,
            int criticalHit,
            boolean burned,
            boolean stab,
            Effectivity eftype)
    {
        level = Utils.range(1, 100, level);
        power = Utils.range(1, 255, power);
        attack = Utils.range(1, 9999, attack);
        defense = Utils.range(1, 9999, defense);
        criticalHit = Utils.range(0, 5, criticalHit);
        
        // base
        int dam = ((level * 2 / 5) + 2) * power * attack / 5 / defense * 2;
        
        // burned
        dam = (burned ? dam / 2 : dam) + 20;
        
        // criticalHit
        if(isCriticalHit(rng, criticalHit))
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
    
    private static boolean isCriticalHit(RNG rng, int ratio)
    {
        switch(ratio)
        {
            default: return false;
            case 1: return rng.d16(1);
            case 2: return rng.d8(1);
            case 3: return rng.d4(1);
            case 4: return rng.d2(1);
            case 5: return true;
        }
    }
}
