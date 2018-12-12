/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.utils.Utils;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public abstract class SecondaryEffect implements Effect
{
    public static final SecondaryEffect EMPTY_EFFECT = EmptySecondaryEffect.INSTANCE;
    
    private int probability;
    
    public final int getProbability() { return probability; }
    public final void setProbability(int probability) { this.probability = Utils.range(1, 100, probability); }
    
    public abstract SecondaryEffectType getType();
    
    public static final UDLValue serialize(SecondaryEffect eff)
    {
        return null;
    }
    
    public static final SecondaryEffect unserialize(UDLValue base)
    {
        return null;
    }
    
    public enum SecondaryEffectType
    {
        EMPTY_EFFECT,
        STAT_ALTERATION,
        WEATHER_ALTERATION,
        STATE_ALTERATION,
        SPECIAL_EFFECT;
        
        private static final SecondaryEffectType[] VALUES = values();
        public static final SecondaryEffectType decode(int id)
        {
            return id < 0 || id >= VALUES.length ? EMPTY_EFFECT : VALUES[id];
        }
    }
}
