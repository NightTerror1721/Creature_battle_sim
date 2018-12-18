/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.AttackModel;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class FixedDamageEffect extends FixedLevelDamageEffect
{
    @Property(set = "setFixedDamageValue")
    private int fixedDamage = 1;
    
    public final void setFixedDamageValue(int value) { this.fixedDamage = Math.max(1, value); }
    public final int getFixedDamageValue() { return fixedDamage; }
    
    @Override
    public final DamageEffectType getType() { return DamageEffectType.FIXED; }

    @Override
    public final DamageType getDamageType() { return DamageType.INDIRECT; }
    
    @Override
    final int computeDamage(FighterTurnState state) { return Math.max(1, fixedDamage); }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        return "Causa " + fixedDamage + " puntos de daño al objetivo";
    }
    
}
