/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.StatId;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class SpecialSecondaryEffect extends SecondaryEffect
{
    @Property
    private SpecialEffectId effect;
    
    
    public final void setEffect(SpecialEffectId effect) { this.effect = effect; }
    public final SpecialEffectId getEffect() { return effect; }
    
    @Override
    public final SecondaryEffectType getType() { return SecondaryEffectType.SPECIAL_EFFECT; }

    @Override
    public final void apply(AttackModel attack, FighterTurnState state)
    {
        if(effect == null)
            return;
        
        switch(effect)
        {
            case EFFECT_CURSE: {
                if(state.self.hasType(ElementalType.GHOST))
                {
                    if(state.self.getCurrentHealthPointsPercentage() > 0.5f)
                    {
                        state.bcm.damage(state.self, state.self.getPercentageMaxHealthPoints(0.5f))
                                .waitTime(500)
                                .message(state.self.getName() + " consume la mitad de sus PS y maldice a su objetivo.")
                                .waitTime(1000)
                                .curse(state.enemy);
                    }
                    else
                    {
                        state.bcm.message("El ataque falló.").waitTime(1000);
                    }
                }
                else
                {
                    state.bcm.playSound("decrease")
                            .statModification(state.self, StatId.SPEED, -1)
                            .message("La velocidad de " + state.self.getName() + " bajó.")
                            .waitTime(1000)
                            .statModification(state.self, StatId.ATTACK, 1)
                            .statModification(state.self, StatId.DEFENSE, 1)
                            .message("El ataque y la defensa de " + state.self.getName() + " han subido.")
                            .waitTime(1000);
                }
            } break;
        }
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        if(effect == null)
            return AIScore.minimum();
        
        switch(effect)
        {
            case EFFECT_CURSE: {
                if(state.self.hasType(ElementalType.GHOST))
                {
                    if(state.self.getCurrentHealthPointsPercentage() > 0.5)
                        return AIScore.medium(false).multiply(state.self.getCurrentHealthPointsPercentage());
                    return AIScore.minimum();
                }
                else
                {
                    float ratio = StatAlterationSecondaryEffect.createRatio(this, 1f);
                    float result = 0f;
                    
                    result += StatAlterationSecondaryEffect.computeStatAIScore(this, state, intel, StatId.ATTACK, StatId.DEFENSE, 1, ratio, true);
                    result += StatAlterationSecondaryEffect.computeStatAIScore(this, state, intel, StatId.DEFENSE, StatId.ATTACK, 1, ratio, true);
                    result += StatAlterationSecondaryEffect.computeStatAIScore(this, state, intel, StatId.SPEED, StatId.SPEED, -1, ratio, true);
                    result = Utils.range(-1f, 1f, result);
                    
                    return AIScore.medium(false).multiply(result);
                }
            }
        }
        
        return AIScore.minimum();
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        if(effect == null)
            return "";
        
        switch(effect)
        {
            case EFFECT_CURSE: return "Causa un efecto diferente dependiendo de si el usuario es un fantasma o no.";
            default: return "";
        }
    }
    
    
    public enum SpecialEffectId
    {
        EFFECT_CURSE;
    }
}
