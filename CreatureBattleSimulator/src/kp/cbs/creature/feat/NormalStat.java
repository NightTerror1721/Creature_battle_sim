/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.creature.Nature;
import kp.cbs.utils.Formula;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public abstract class NormalStat extends Stat
{
    private int modifLevel;
    
    private NormalStat() {}
    
    
    @Override
    public final void update(int level, Nature nature)
    {
        setValue(Formula.statValue(getStatId(), level, base, gen, ab, nature, modifLevel));
    }
    
    public final boolean modifyStat(int levels)
    {
        levels = Utils.range(-6, 6, levels);
        if(levels > 0)
        {
            if(modifLevel < 6)
            {
                modifLevel = Math.min(6, modifLevel + levels);
                return true;
            }
            
        }
        else if(levels < 0)
        {
            if(modifLevel < 6)
            {
                modifLevel = Math.max(-6, modifLevel + levels);
                return true;
            }
        }
        return false;
    }
    
    public final void clearAlterations()
    {
        modifLevel = 0;
    }
    
    public final int getAlterationLevels() { return modifLevel; }
    
    
    public static final class Attack extends NormalStat
    {
        @Override public final StatId getStatId() { return StatId.ATTACK; }
    }
    
    public static final class Defense extends NormalStat
    {
        @Override public final StatId getStatId() { return StatId.DEFENSE; }
    }
    
    public static final class SpecialAttack extends NormalStat
    {
        @Override public final StatId getStatId() { return StatId.SPECIAL_ATTACK; }
    }
    
    public static final class SpecialDefense extends NormalStat
    {
        @Override public final StatId getStatId() { return StatId.SPECIAL_DEFENSE; }
    }
    
    public static final class Speed extends NormalStat
    {
        @Override public final StatId getStatId() { return StatId.SPEED; }
    }
}
