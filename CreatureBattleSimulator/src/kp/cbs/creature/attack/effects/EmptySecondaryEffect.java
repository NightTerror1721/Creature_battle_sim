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
public class EmptySecondaryEffect extends SecondaryEffect
{
    static final EmptySecondaryEffect INSTANCE = new EmptySecondaryEffect();
    
    private EmptySecondaryEffect() {}
    
    @Override
    public final SecondaryEffectType getType() { return SecondaryEffectType.EMPTY_EFFECT; }

    @Override
    public final void apply(AttackModel attack, FighterTurnState state) {}

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        return AIScore.zero();
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        return "";
    }

    @Override
    public final String toString() { return "Ninguno"; }
    
}
