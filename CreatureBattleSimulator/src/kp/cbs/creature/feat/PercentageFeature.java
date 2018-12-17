/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.utils.Formula;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public final class PercentageFeature extends BaseFeature
{
    private final boolean inverse;
    private int modifLevel;
    
    public PercentageFeature(boolean isInverse) { this.inverse = isInverse; }
    
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
    
    public final float getModification() { return Formula.percentageFeatModif(inverse && modifLevel != 0 ? -modifLevel : modifLevel); }
}
