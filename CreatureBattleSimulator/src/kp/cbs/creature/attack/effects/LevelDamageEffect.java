/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.AttackModel;

/**
 *
 * @author Asus
 */
public final class LevelDamageEffect extends FixedLevelDamageEffect
{
    static final LevelDamageEffect INSTANCE = new LevelDamageEffect();
    
    private LevelDamageEffect() {}
    
    @Override
    public final DamageEffectType getType() { return DamageEffectType.LEVEL; }

    @Override
    public final DamageType getDamageType() { return DamageType.INDIRECT; }
    
    @Override
    final int computeDamage(FighterTurnState state) { return state.self.getLevel() * 20; }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        return "Causa un daño equivalente a NivelUsuario x 20.";
    }
    
}
