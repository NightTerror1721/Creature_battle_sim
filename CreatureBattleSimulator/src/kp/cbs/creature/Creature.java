/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.util.Objects;
import kp.cbs.creature.feat.FeatureManager;
import kp.cbs.creature.feat.HealthPoints;
import kp.cbs.creature.feat.NormalStat;
import kp.cbs.creature.feat.PercentageFeature;
import kp.cbs.creature.feat.Stat;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.race.Race;
import kp.cbs.creature.race.RaceReference;
import kp.cbs.creature.state.StateManager;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "afterInject")
public final class Creature
{
    @Property(set = "setName")
    private String name = "";
    
    @Property
    private RaceReference race;
    
    @Property(name = "features")
    private FeatureManager feats;
    
    @Property
    private ExperienceManager exp;
    
    @Property(set = "setNature", invalidEnumValue = "HARDY")
    private Nature nature = Nature.HARDY;
    
    private final StateManager state = new StateManager();
    
    private int fighterId;
    
    private Creature() {}
    
    public static final Creature create(Race race, int level)
    {
        Creature c = new Creature();
        c.race = new RaceReference(race);
        c.feats = FeatureManager.create();
        c.exp = new ExperienceManager();
        
        c.exp.init(race.getGrowth(), level);
        
        return c;
    }
    public static final Creature create(String race, int level) { return create(Race.getRace(race), level); }
    
    public final void setName(String name) { this.name = Objects.requireNonNull(name); }
    public final String getName() { return name; }
    
    public final void setNature(Nature nature) { this.nature = Objects.requireNonNull(nature); }
    public final Nature getNature() { return nature; }
    
    public final void setRace(Race race) { this.race = new RaceReference(race); }
    public final Race getRace() { return race.getRace(); }
    
    public final FeatureManager getFeaturesManager() { return feats; }
    
    public final HealthPoints getHealthPoints() { return feats.getHealthPoints(); }
    public final NormalStat getAttack() { return feats.getAttack(); }
    public final NormalStat getDefense() { return feats.getDefense(); }
    public final NormalStat getSpecialAttack() { return feats.getSpecialAttack(); }
    public final NormalStat getSpecialDefense() { return feats.getSpecialDefense(); }
    public final NormalStat getSpeed() { return feats.getSpeed(); }
    
    public final Stat getStat(StatId statId) { return feats.getStat(statId); }
    
    public final ExperienceManager getExperienceManager() { return exp; }
    
    public final Growth getGrowth() { return exp.getGrowth(); }
    public final int getLevel() { return exp.getLevel(); }
    public final int getExperience() { return exp.getExperience(); }
    public final int getNextLevelExperience() { return exp.getNextLevelExperience(); }
    
    public final PercentageFeature getAccuracy() { return state.getAccuracy(); }
    public final PercentageFeature getEvasion() { return state.getEvasion(); }
    
    public final void setCriticalHitBonus(boolean flag) { state.setCriticalHitBonus(flag); }
    public final boolean isCriticalHitBonus() { return state.isCriticalHitBonus(); }
    
    public final void setResting(boolean flag) { state.setResting(flag); }
    public final boolean isResting() { return state.isResting(); }
    
    public final void setIntimidated(boolean flag) { state.setIntimidated(flag); }
    public final boolean isIntimidated() { return state.isIntimidated(); }
    
    
    public final void setFighterId(int id) { this.fighterId = id; }
    public final int getFighterId() { return fighterId; }
    
    
    
    public final void updateAll()
    {
        feats.update(exp.getLevel(), nature);
    }
    
    public final void clearAll()
    {
        
    }
    
    
    
    
    private static Creature injector()
    {
        return new Creature();
    }
    
    private void afterInject()
    {
        clearAll();
        updateAll();
    }
}
