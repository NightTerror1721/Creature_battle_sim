/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.utils.Serializer;
import kp.udl.data.UDLObject;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public abstract class DamageEffect implements Effect
{
    public static final DamageEffect NO_DAMAGE = NoDamageEffect.INSTANCE;
    public static final DamageEffect LEVEL_DAMAGE = LevelDamageEffect.INSTANCE;
    
    public abstract DamageEffectType getType();
    
    public abstract DamageType getDamageType();
    
    public static final UDLValue serialize(DamageEffect eff)
    {
        UDLObject obj = new UDLObject();
        obj.setInt("id", eff.getType().ordinal());
        switch(eff.getType())
        {
            case NORMAL:
            case FIXED:
            case HEAL:
                obj.set("effect", Serializer.extract(eff));
                break;
        }
        return obj;
    }
    
    public static final DamageEffect unserialize(UDLValue base)
    {
        try
        {
            switch(DamageEffectType.decode(base.getInt("id")))
            {
                default:
                case NO_DAMAGE: return NO_DAMAGE;
                case NORMAL: return Serializer.inject(base.get("effect"), NormalDamageEffect.class);
                case LEVEL: return LEVEL_DAMAGE;
                case FIXED: return Serializer.inject(base.get("effect"), FixedDamageEffect.class);
                case HEAL: return Serializer.inject(base.get("effect"), HealDamageEffect.class);
            }
        }
        catch(Throwable ex)
        {
            ex.printStackTrace(System.err);
            return NO_DAMAGE;
        }
    }
    
    public enum DamageEffectType
    {
        NO_DAMAGE,
        NORMAL,
        LEVEL,
        FIXED,
        HEAL;
        
        private static final DamageEffectType[] VALUES = values();
        public static final DamageEffectType decode(int id)
        {
            return id < 0 || id >= VALUES.length ? NO_DAMAGE : VALUES[id];
        }
    }
    
    public enum DamageType
    {
        NO_DAMAGE,
        PHYSICAL,
        SPECIAL,
        INDIRECT;
    }
}
