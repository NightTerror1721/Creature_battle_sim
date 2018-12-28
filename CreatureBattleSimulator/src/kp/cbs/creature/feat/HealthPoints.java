/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.creature.Nature;
import kp.cbs.creature.race.Race;
import kp.cbs.utils.Formula;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class HealthPoints extends Stat
{
    @Property(set = "setCurrentHealthPoints")
    private int current;
    
    public final void setCurrentHealthPoints(int points) { this.current = Utils.range(0, getValue(), points); }
    public final int getCurrentHealthPoints() { return current; }
    
    public final int getMaxHealthPoints() { return getValue(); }
    
    public final void damage(int points) { setCurrentHealthPoints(current - points); }
    public final void kill() { this.current = 0; }
    
    public final void heal(int points) { setCurrentHealthPoints(current + points); }
    public final void fullHeal() { this.current = getValue(); }
    
    @Override
    public final StatId getStatId() { return StatId.HEALTH_POINTS; }

    @Override
    public final void update(Race race, int level, Nature nature)
    {
        base = race.getBase(getStatId());
        setValue(Formula.hpValue(level, base, gen, ab));
        if(current > getValue())
            current = getValue();
    }
    
}
