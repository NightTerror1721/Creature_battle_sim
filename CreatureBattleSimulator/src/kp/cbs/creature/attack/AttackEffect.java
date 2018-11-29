/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Objects;
import kp.cbs.creature.attack.flag.Flag;

/**
 *
 * @author Asus
 */
public final class AttackEffect
{
    private final AttackEffectModel model;
    private final Flag[] flags;
    private String description;
    
    private AttackEffect(AttackEffectModel model, int[] values)
    {
        this.model = Objects.requireNonNull(model);
        this.flags = model.generateFlags(values);
    }
    
    public final Flag getFlag(int index) { return flags[index]; }
    public final <T> T getFlagValue(int index) { return flags[index].getValue(); }
}
