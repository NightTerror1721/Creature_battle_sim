/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.battle.FighterTurnState;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.elements.ElementalType;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public class WeatherSecondaryEffect extends SecondaryEffect
{
    @Property
    private WeatherId weather = WeatherId.RAIN;
    
    public final void setWeather(WeatherId weather) { this.weather = weather; }
    public final WeatherId getWeather() { return weather; }
    
    @Override
    public final SecondaryEffectType getType() { return SecondaryEffectType.WEATHER_ALTERATION; }

    @Override
    public final void apply(AttackModel attack, FighterTurnState state)
    {
        if(weather == null)
            return;
        
        if(weather == state.getWeather())
        {
            state.bcm.message("Falló porque el tiempo actual ya es " + weather);
            return;
        }
        state.bcm.weatherChange(weather, 5);
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        if(weather == null || state.getWeather() == weather)
            return AIScore.zero();
        
        float ratio = 0f;
        switch(weather)
        {
            case RAIN:
                if(state.self.getAttackManager().hasAttackWithType(ElementalType.WATER))
                {
                    ratio += 1f;
                    if(intel.isNormalOrGreater() && state.enemy.effectivity(ElementalType.WATER).isSuperEffective())
                        ratio += 0.5f;
                }
                if(intel.isNormalOrGreater())
                {
                    if(state.enemy.getAttackManager().hasAttackWithType(ElementalType.WATER))
                    {
                        ratio -= 0.5f;
                        if(state.self.effectivity(ElementalType.WATER).isSuperEffective())
                            ratio -= 0.5f;
                    }
                    if(state.enemy.getAttackManager().hasAttackWithType(ElementalType.FIRE))
                        ratio += 0.25f;
                }
                break;
                
            case INTENSE_SUN:
                if(state.self.getAttackManager().hasAttackWithType(ElementalType.FIRE))
                {
                    ratio += 1f;
                    if(intel.isNormalOrGreater() && state.enemy.effectivity(ElementalType.FIRE).isSuperEffective())
                        ratio += 0.5f;
                }
                if(intel.isNormalOrGreater())
                {
                    if(state.enemy.getAttackManager().hasAttackWithType(ElementalType.FIRE))
                    {
                        ratio -= 0.5f;
                        if(state.self.effectivity(ElementalType.FIRE).isSuperEffective())
                            ratio -= 0.5f;
                    }
                    if(state.enemy.getAttackManager().hasAttackWithType(ElementalType.WATER))
                        ratio += 0.25f;
                }
                break;
            
            case SANDSTORM:
                if(state.self.hasAnyType(ElementalType.ROCK, ElementalType.GROUND))
                    ratio += 1f;
                else if(!state.self.hasType(ElementalType.STEEL))
                    ratio -= 0.5f;
                
                if(intel.isNormalOrGreater())
                {
                    if(state.enemy.hasAnyType(ElementalType.ROCK, ElementalType.GROUND))
                        ratio -= 0.8f;
                    else if(state.self.hasType(ElementalType.STEEL))
                        ratio -= 0.4f;
                    else ratio += 0.25f;
                }
                break;
                
            case HAIL:
                if(state.self.hasType(ElementalType.ICE))
                    ratio += 1f;
                else ratio -= 0.5f;
                
                if(intel.isNormalOrGreater())
                {
                    if(state.enemy.hasType(ElementalType.ICE))
                        ratio -= 0.5f;
                    else ratio += 0.5f;
                }
                break;
                
            case FOG:
                if(state.self.hasType(ElementalType.DARK))
                    ratio += 1f;
                else if(state.self.hasType(ElementalType.POISON))
                    ratio += 0.5f;
                
                if(intel.isNormalOrGreater())
                {
                    if(state.enemy.hasType(ElementalType.DARK))
                        ratio -= 0.5f;
                    else if(state.enemy.hasType(ElementalType.POISON))
                        ratio -= 0.25f;
                }
                break;
                
            case ELECTRIC_STORM:
                if(state.self.getAttackManager().hasAttackWithType(ElementalType.ELECTRIC))
                {
                    ratio += 1f;
                    if(intel.isNormalOrGreater() && state.enemy.effectivity(ElementalType.WATER).isSuperEffective())
                        ratio += 0.5f;
                }
                if(state.self.hasType(ElementalType.ELECTRIC))
                    ratio += 0.5f;
                
                if(intel.isNormalOrGreater())
                {
                    if(state.enemy.getAttackManager().hasAttackWithType(ElementalType.ELECTRIC))
                    {
                        ratio -= 0.75f;
                        if(state.self.effectivity(ElementalType.ELECTRIC).isSuperEffective())
                            ratio -= 0.5f;
                    }
                    if(state.enemy.hasType(ElementalType.ELECTRIC))
                        ratio -= 0.25f;
                }
        }
        
        return AIScore.fourth(false).multiply(ratio);
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        return "Produce " + weather + ".";
    }
    
}
