/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.creature.Nature;
import kp.cbs.creature.race.Race;
import kp.cbs.utils.Formula;
import kp.cbs.utils.RNG;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public abstract class Stat extends BaseFeature
{
    @Property(set = "setBasePoints")
    protected int base = 1;
    
    @Property(name = "genetic", set = "setGeneticPoints")
    protected int gen;
    
    @Property(name = "ability", set = "setAbilityPoints")
    protected int ab;
    
    private int value;
    
    
    public abstract StatId getStatId();
    public abstract void update(Race race, int level, Nature nature);
    
    public final void setBasePoints(int points) { this.base = Math.max(1, points); }
    public final int getBasePoints() { return base; }
    
    public final void setGeneticPoints(int points) { this.gen = Utils.range(0, 32, points); }
    public final int getGeneticPoints() { return gen; }
    
    public final void setAbilityPoints(int points) { this.ab = Utils.range(0, Formula.MAX_STAT_ABILITY_POINTS, points); }
    public final void addAbilityPoints(int points) { this.ab = Math.min(Formula.MAX_STAT_ABILITY_POINTS, ab + Math.max(0, points)); }
    public final int getAbilityPoints() { return ab; }
    
    final void setValue(int value) { this.value = Math.max(0, value); }
    public final int getValue() { return value; }
    
    public abstract int getPureValue(int level, Nature nature);
    
    final void fillRandom(RNG rng)
    {
        setGeneticPoints(rng.d(33));
    }
    
}
