/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.utils.Serializer;
import kp.cbs.utils.Utils;
import kp.udl.data.UDLObject;
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
        return new UDLObject()
                .setInt("id", eff.getType().ordinal())
                .set("effect", Serializer.extract(eff));
    }
    
    public static final SecondaryEffect unserialize(UDLValue base)
    {
        try
        {
            switch(SecondaryEffectType.decode(base.getInt("id")))
            {
                default:
                case EMPTY_EFFECT: return EMPTY_EFFECT;
                case STAT_ALTERATION: return Serializer.inject(base.get("effect"), StatAlterationSecondaryEffect.class);
                case WEATHER_ALTERATION: return Serializer.inject(base.get("effect"), WeatherSecondaryEffect.class);
                case STATE_ALTERATION: return Serializer.inject(base.get("effect"), StateAlterationSecondaryEffect.class);
                case SPECIAL_EFFECT: return Serializer.inject(base.get("effect"), SpecialSecondaryEffect.class);
            }
        }
        catch(Throwable ex)
        {
            ex.printStackTrace(System.err);
            return EMPTY_EFFECT;
        }
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
