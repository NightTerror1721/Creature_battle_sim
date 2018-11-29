/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.elements;

/**
 *
 * @author Asus
 */
public final class Effectivity
{
    public static final Effectivity NOT_EFFECTIVE = new Effectivity(0f);
    public static final Effectivity NOT_VERY_EFFECTIVE = new Effectivity(0.5f);
    public static final Effectivity NORMAL_EFFECTIVE = new Effectivity(1f);
    public static final Effectivity SUPER_EFFECTIVE = new Effectivity(2f);
    
    private final float multiplier;
    
    private Effectivity(float multiplier)
    {
        this.multiplier = multiplier;
    }
    
    public final float multiplier() { return multiplier; }
    
    public final Effectivity combine(Effectivity e)
    {
        return new Effectivity(multiplier * e.multiplier);
    }
    
    public final boolean isNotEffective() { return multiplier <= 0f; }
    public final boolean isNotVeryEffective() { return multiplier > 0f && multiplier < 1f; }
    public final boolean isNormalEffective() { return multiplier == 1f; }
    public final boolean isSuperEffective() { return multiplier > 1f; }
    
    /*public static final Effectivity compute(float multiplier)
    {
        if(multiplier <= 0f)
            return NOT_EFFECTIVE;
        else if(multiplier < 1f)
            return NOT_VERY_EFFECTIVE;
        else if(multiplier > 1f)
            return VERY_EFFECTIVE;
        return NORMAL_EFFECTIVE;
    }*/
}
