/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;

/**
 *
 * @author Asus
 */
abstract class FixedLevelDamageEffect extends DamageEffect
{
    abstract int computeDamage(FighterTurnState state);
    
    @Override
    public final void apply(AttackModel attack, FighterTurnState state)
    {
        if(state.enemy.effectivity(attack.getElementalType()).isNotEffective())
        {
            state.bcm.message("¡NO AFECTA a " + state.enemy.getName() + "!");
        }
        else
        {
            int dam = computeDamage(state);
            state.bcm.playSound("normal_effective").damage(state.enemy, dam);
        }
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        if(state.enemy.effectivity(attack.getElementalType()).isNotEffective())
            return AIScore.zero();
        
        int dam = computeDamage(state);
        float current = state.enemy.getCurrentHealthPoints();
        
        if(intel.isHightOrGreater())
        {
            return dam < current ? AIScore.maximum().multiply(dam / current) : AIScore.maximum();
        }
        else if(intel.isNormalOrGreater())
        {
            return current <= dam ? AIScore.maximum() : AIScore.eighth(false).divide(8);
        }
        else
        {
            if(current <= dam)
                return AIScore.random(state.rng, false).multiply(3f / 5f);
            int ratio = 32 - ((int) (state.enemy.getCurrentHealthPointsPercentage() * 32));
            return AIScore.random(state.rng, false).multiply(ratio / 64f);
        }
    }
}
