/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.creature.Nature;
import kp.cbs.utils.Formula;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "clearAllAlterations")
public final class FeatureManager
{
    @Property private HealthPoints hp;
    @Property private NormalStat.Attack attack;
    @Property private NormalStat.Defense defense;
    @Property private NormalStat.SpecialAttack specialAttack;
    @Property private NormalStat.SpecialDefense specialDefense;
    @Property private NormalStat.Speed speed;
    
    private FeatureManager() {}
    
    public static final FeatureManager create()
    {
        FeatureManager fm = new FeatureManager();
        
        fm.hp = new HealthPoints();
        fm.attack = new NormalStat.Attack();
        fm.defense = new NormalStat.Defense();
        fm.specialAttack = new NormalStat.SpecialAttack();
        fm.specialDefense = new NormalStat.SpecialDefense();
        fm.speed = new NormalStat.Speed();
        
        return fm;
    }
    
    public final HealthPoints getHealthPoints() { return hp; }
    public final NormalStat getAttack() { return attack; }
    public final NormalStat getDefense() { return defense; }
    public final NormalStat getSpecialAttack() { return specialAttack; }
    public final NormalStat getSpecialDefense() { return specialDefense; }
    public final NormalStat getSpeed() { return speed; }
    
    public final Stat getStat(StatId statId)
    {
        switch(statId)
        {
            case HEALTH_POINTS: return hp;
            case ATTACK: return attack;
            case DEFENSE: return defense;
            case SPECIAL_ATTACK: return specialAttack;
            case SPECIAL_DEFENSE: return specialDefense;
            case SPEED: return speed;
            default: throw new IllegalStateException();
        }
    }
    
    public final void clearAllAlterations()
    {
        attack.clearAlterations();
        defense.clearAlterations();
        specialAttack.clearAlterations();
        specialDefense.clearAlterations();
        speed.clearAlterations();
    }
    
    public final void update(int level, Nature nature)
    {
        hp.update(level, nature);
        attack.update(level, nature);
        defense.update(level, nature);
        specialAttack.update(level, nature);
        specialDefense.update(level, nature);
        speed.update(level, nature);
    }
    
    public final int getStatSum()
    {
        return hp.base + attack.base + defense.base + specialAttack.base + specialDefense.base + speed.base;
    }
    
    public final int computeExperienceBase()
    {
        int sum = getStatSum();
        return Formula.creatureExpBase(sum);
    }
    
    
    private static FeatureManager injector()
    {
        FeatureManager fm = new FeatureManager();
        
        return fm;
    }
}
