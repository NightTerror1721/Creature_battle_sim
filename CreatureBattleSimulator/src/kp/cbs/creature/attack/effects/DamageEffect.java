/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

/**
 *
 * @author Asus
 */
public abstract class DamageEffect implements Effect
{
    public static final DamageEffect NO_DAMAGE = NoDamageEffect.INSTANCE;
    
    public abstract DamageEffectType getType();
    
    public enum DamageEffectType { NO_DAMAGE, PHYSICAL, SPECIAL, LEVEL, FIXED; }
}
