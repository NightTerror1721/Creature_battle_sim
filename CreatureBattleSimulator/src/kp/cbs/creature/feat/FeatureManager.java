/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import java.util.StringJoiner;
import kp.cbs.creature.Nature;
import kp.cbs.creature.race.Race;
import kp.cbs.utils.Formula;
import kp.cbs.utils.RNG;
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
    
    public final void update(Race race, int level, Nature nature)
    {
        hp.update(race, level, nature);
        attack.update(race, level, nature);
        defense.update(race, level, nature);
        specialAttack.update(race, level, nature);
        specialDefense.update(race, level, nature);
        speed.update(race, level, nature);
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
    
    public final int computeAbilityPointsBase()
    {
        var sum = getStatSum();
        return Formula.creatureStatAbilityBase(sum);
    }
    
    public final int getAbilityPointsCount()
    {
        return hp.ab + attack.ab + defense.ab + specialAttack.ab + specialDefense.ab + speed.ab;
    }
    
    public final boolean hasMaxAbilityPoints()
    {
        return getAbilityPointsCount() >= Formula.MAX_ABILITY_POINTS;
    }
    
    public final String getPowerupAbbreviations()
    {
        var joiner = new StringJoiner(" ");
        addAbbreviation(joiner, attack);
        addAbbreviation(joiner, defense);
        addAbbreviation(joiner, specialAttack);
        addAbbreviation(joiner, specialDefense);
        addAbbreviation(joiner, speed);
        return joiner.toString();
        
    }
    private static void addAbbreviation(StringJoiner joiner, NormalStat stat)
    {
        var abb = stat.getPowerupAbbreviation();
        if(!abb.isBlank())
            joiner.add(abb);
    }
    
    public final void randomizeStatGenetics(RNG rng)
    {
        hp.fillRandom(rng);
        attack.fillRandom(rng);
        defense.fillRandom(rng);
        specialAttack.fillRandom(rng);
        specialDefense.fillRandom(rng);
        speed.fillRandom(rng);
    }
    
    
    private static FeatureManager injector()
    {
        FeatureManager fm = new FeatureManager();
        
        return fm;
    }
}
