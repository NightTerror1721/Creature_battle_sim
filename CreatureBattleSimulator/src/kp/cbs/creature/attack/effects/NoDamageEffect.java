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
public final class NoDamageEffect extends DamageEffect
{
    public static final NoDamageEffect INSTANCE = new NoDamageEffect();
    
    private NoDamageEffect() {}
    
    @Override
    public final DamageEffectType getType() { return DamageEffectType.NO_DAMAGE; }

    @Override
    public void apply(AttackModel attack, FighterTurnState state) {}

    @Override
    public final AIScore computeAIScore(AttackModel attack, FighterTurnState state) { return AIScore.zero(); }

    @Override
    public final String generateDescription(AttackModel attack) { return ""; }
    
}
