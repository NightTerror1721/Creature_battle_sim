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
public interface Effect
{
    void apply(AttackModel attack, FighterTurnState state);
    
    AIScore computeAIScore(AttackModel attack, FighterTurnState state);
    
    String generateDescription(AttackModel attack);
}
