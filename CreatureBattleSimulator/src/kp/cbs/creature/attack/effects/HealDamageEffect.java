/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import java.util.List;
import java.util.StringJoiner;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.utils.Utils;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class HealDamageEffect extends DamageEffect
{
    private int healPer;
    private final int[] wHealPers = new int[WeatherId.count()];
    
    public final void setNormalHealPercentage(int value) { this.healPer = Utils.range(0, 256, value); }
    public final int getNormalHealPercentaage() { return healPer; }
    
    public final void setWeatherHealPercentage(WeatherId weather, int value) { this.wHealPers[weather.ordinal()] = Utils.range(0, 256, value); }
    public final int getWeatherHealPercentage(WeatherId weather) { return wHealPers[weather.ordinal()]; }
    
    @Override
    public final DamageEffectType getType() { return DamageEffectType.HEAL; }

    @Override
    public final DamageType getDamageType() { return DamageType.NO_DAMAGE; }

    @Override
    public final void apply(AttackModel attack, FighterTurnState state)
    {
        if(state.self.isFullHealth())
        {
            state.bcm.message("No surtió ningun efecto curativo.");
            return;
        }
        
        int healRatio;
        WeatherId weather = state.getWeather();
        if(weather == null)
            healRatio = healPer;
        else healRatio = wHealPers[weather.ordinal()];
        
        int points = (int) ((healRatio / 256f) * state.self.getMaxHealthPoints());
        if(points < 1)
        {
            state.bcm.message("No surtió ningun efecto curativo.");
            return;
        }
        
        state.bcm.playSound("heal").heal(state.self, points)
                .waitTime(500)
                .message(state.self.getName() + " ha recuperado salud.");
    }

    @Override
    public final AIScore computeAIScore(AttackModel attack, AttackTurn turn, FighterTurnState state, AIIntelligence intel)
    {
        if(intel.isDummy())
            return AIScore.random(state.rng, false);
        
        float remainingHealth = state.self.getCurrentHealthPointsPercentage();
        if(intel.isNormalOrLess())
        {
            if(remainingHealth > 0.5f)
                return AIScore.zero();
            return AIScore.maximum().multiply(1f - remainingHealth * 2f);
        }
        else
        {
            if(remainingHealth > 0.8f)
                return AIScore.zero();
            return AIScore.maximum().multiply(1f - remainingHealth);
        }
    }

    @Override
    public final String generateDescription(AttackModel attack)
    {
        StringJoiner joiner = new StringJoiner(", ", "Cura una cierta cantidad de salud en funcion del clima: ", ".");
        joiner.add(healPer <= 0 ? "No recupera salud si no hay clima" : "Recupera un " + bytePercentage(healPer) + " si no hay clima");
        for(int i=0;i<wHealPers.length;i++)
        {
            WeatherId w = WeatherId.decode(i);
            int value = wHealPers[i];
            joiner.add(value <= 0 ? ("No recupera salud en " + w) : ("Recupera " + bytePercentage(value) + " en " + w));
        }
        return joiner.toString();
    }
    
    private static String bytePercentage(int value)
    {
        return ((int) (value / 256f * 100)) + "%";
    }
    
    public static final AutowiredSerializer<HealDamageEffect> SERIALIZER = new AutowiredSerializer<HealDamageEffect>(HealDamageEffect.class)
    {
        @Override
        public final UDLValue serialize(HealDamageEffect value)
        {
            UDLValue[] array = new UDLValue[value.wHealPers.length + 1];
            array[0] = UDLValue.valueOf(value.healPer);
            for(int i=0;i<value.wHealPers.length;i++)
                array[i + 1] = UDLValue.valueOf(value.wHealPers[i]);
            return UDLValue.valueOf(array);
        }
        
        @Override
        public final HealDamageEffect unserialize(UDLValue value)
        {
            HealDamageEffect eff = new HealDamageEffect();
            List<UDLValue> list = value.getList();
            
            int i = 0;
            for(UDLValue v : list)
            {
                if(i == 0)
                    eff.healPer = v.getInt();
                else eff.wHealPers[i - 1] = v.getInt();
                i++;
            }
            return eff;
        }
    };
}
