/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.util.Objects;
import kp.cbs.utils.Formula;
import kp.cbs.utils.Utils;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(afterBuild = "afterInject")
public final class ExperienceManager
{
    @Property
    private Growth growth;
    
    @Property
    private int level;
    
    @Property(name = "experience")
    private int exp;
    
    private int maxExp;
    
    
    public final void init(Growth growth, int level)
    {
        this.growth = Objects.requireNonNull(growth);
        setLevel(level);
    }
    
    public final Growth getGrowth() { return growth; }
    
    public final int getLevel() { return level; }
    
    public final int getExperience() { return exp; }
    
    public final int getNextLevelExperience() { return maxExp; }
    
    
    private void checkExp()
    {
        while(level < 100 && exp > maxExp)
        {
            level++;
            if(level == 100)
            {
                exp = Formula.expToLevel(growth, 100);
                maxExp = 0;
            }
            else maxExp = Formula.expToLevel(growth, level + 1);
        }
    }
    
    public final void setLevel(int level)
    {
        this.level = Utils.range(1, 100, level);
        this.exp = this.level < 2 ? 0 : Formula.expToLevel(growth, this.level);
        this.maxExp = this.level > 99 ? 0 : Formula.expToLevel(growth, this.level + 1);
    }
    
    public final void addExperience(int points)
    {
        this.exp += Math.max(1, points);
        checkExp();
    }
    
    private void afterInject()
    {
        this.level = Utils.range(1, 100, level);
        this.maxExp = this.level > 99 ? 0 : Formula.expToLevel(growth, this.level + 1);
        checkExp();
    }
}
