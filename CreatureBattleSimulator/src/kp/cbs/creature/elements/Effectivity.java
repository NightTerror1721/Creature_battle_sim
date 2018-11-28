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
public enum Effectivity
{
    NOT_EFFECTIVE(0f),
    NOT_VERY_EFFECTIVE(0.5f),
    NORMAL_EFFECTIVE(1f),
    VERY_EFFECTIVE(2f);
    
    private final float multiplier;
    
    private Effectivity(float multiplier)
    {
        this.multiplier = multiplier;
    }
    
    public final float multiplier() { return multiplier; }
    
    public static final float combinedMultiplier(Effectivity e1, Effectivity e2)
    {
        return e1.multiplier * e2.multiplier;
    }
    
    public static final Effectivity compute(float multiplier)
    {
        if(multiplier <= 0f)
            return NOT_EFFECTIVE;
        else if(multiplier < 1f)
            return NOT_VERY_EFFECTIVE;
        else if(multiplier > 1f)
            return VERY_EFFECTIVE;
        return NORMAL_EFFECTIVE;
    }
}
