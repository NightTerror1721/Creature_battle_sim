/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.attack.effects.NormalDamageEffect;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
final class CombatAttackModelGenerator
{
    private CombatAttackModelGenerator() {}
    
    public static final AttackModel generate(int level)
    {
        level = Utils.range(1, 100, level);
        
        AttackModel model = new AttackModel();
        model.setId(0);
        model.setMaxPP(1);
        model.setElementalType(ElementalType.UNKNOWN);
        model.setPower(30 + level / 5);
        model.setName("GOLPEAR");
        model.setPrecision(100);
        model.setPriority(0);
        model.setTurns(1);
        
        AttackTurn turn = model.getTurn(0);
        NormalDamageEffect damage = new NormalDamageEffect();
        damage.setAttackValueToSelf();
        damage.setDefenseValueToSelf();
        damage.setPowerValueDefault();
        damage.setBackwardDamageOrAbsordb(-64);
        turn.setDamageEffect(damage);
        
        return model;
    }
}
