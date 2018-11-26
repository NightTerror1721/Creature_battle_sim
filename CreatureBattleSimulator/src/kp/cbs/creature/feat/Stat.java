/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.creature.Nature;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public abstract class Stat extends BaseFeature
{
    @Property
    protected int base = 1;
    
    @Property(name = "genetic")
    protected int gen;
    
    @Property(name = "ability")
    protected int ab;
    
    private int value;
    
    
    public abstract StatId getStatId();
    public abstract void update(int level, Nature nature);
    
    public final void setBasePoints(int points) { this.base = Math.max(1, points); }
    public final int getBasePoints() { return base; }
    
    public final void setGeneticPoints(int points) { this.gen = Utils.range(0, 32, points); }
    public final int getGeneticPoints() { return gen; }
    
    public final void setAbilityPoints(int points) { this.ab = Utils.range(0, 512, points); }
    public final void addAbilityPoints(int points) { this.ab = Math.min(512, ab + Math.max(0, points)); }
    public final int getAbilityPoints() { return ab; }
    
    final void setValue(int value) { this.value = Math.max(0, value); }
    public final int getValue() { return value; }
    
}
