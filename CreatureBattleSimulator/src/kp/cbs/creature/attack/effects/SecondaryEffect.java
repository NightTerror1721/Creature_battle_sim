/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public abstract class SecondaryEffect implements Effect
{
    private int probability;
    
    public final int getProbability() { return probability; }
    public final void setProbability(int probability) { this.probability = Utils.range(0, 100, probability); }
    
    public abstract SecondaryEffectType getType();
    
    public enum SecondaryEffectType { STAT_ALTERATION, PRECISION_ALTERATION, WEATHER_ALTERATION, STATE_ALTERATION, STATE_CHANGE  }
}
