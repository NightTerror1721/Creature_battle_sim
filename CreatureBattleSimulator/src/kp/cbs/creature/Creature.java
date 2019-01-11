/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.battle.TeamId;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.creature.altered.AlteredState;
import kp.cbs.creature.altered.AlteredStateId;
import kp.cbs.creature.altered.AlteredStateManager;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackManager;
import kp.cbs.creature.attack.AttackManager.SelectedAttack;
import kp.cbs.creature.attack.AttackSlot;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.creature.elements.Effectivity;
import kp.cbs.creature.elements.ElementalManager;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.FeatureManager;
import kp.cbs.creature.feat.HealthPoints;
import kp.cbs.creature.feat.NormalStat;
import kp.cbs.creature.feat.PercentageFeature;
import kp.cbs.creature.feat.Stat;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.race.Race;
import kp.cbs.creature.race.RacePool;
import kp.cbs.creature.state.StateManager;
import kp.cbs.utils.Formula;
import kp.cbs.utils.RNG;
import kp.cbs.utils.Utils;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "afterInject")
public final class Creature implements Comparable<Creature>
{
    @Property
    private UUID id;
    
    @Property(set = "setName")
    private String name = "";
    
    @Property
    private Race race;
    
    @Property(name = "features")
    private FeatureManager feats;
    
    @Property
    private ExperienceManager exp;
    
    @Property(set = "setNature", invalidEnumValue = "HARDY")
    private Nature nature = Nature.HARDY;
    
    @Property
    private AttackManager attacks;
    
    @Property(name = "abilityPoints")
    private int remainingAbPoints;
    
    private final ElementalManager types = new ElementalManager();
    
    private final StateManager state = new StateManager();
    
    private final AlteredStateManager altered = new AlteredStateManager();
    
    private TeamId fighterId;
    
    private Creature() {}
    
    public static final Creature create(Race race, int level)
    {
        Creature c = new Creature();
        c.id = UUID.randomUUID();
        c.race = Objects.requireNonNull(race);
        c.feats = FeatureManager.create();
        c.exp = new ExperienceManager();
        c.attacks = new AttackManager();
        
        c.exp.init(race.getGrowth(), level);
        c.clearAll();
        c.updateAll();
        
        return c;
    }
    public static final Creature create(int raceId, int level) { return create(RacePool.getRace(raceId), level); }
    
    public final UUID getId() { return id; }
    
    public final void setName(String name) { this.name = Objects.requireNonNull(name); }
    public final String getName() { return name; }
    
    public final void setNature(Nature nature) { this.nature = Objects.requireNonNull(nature); }
    public final Nature getNature() { return nature; }
    
    public final void setRace(Race race) { this.race = Objects.requireNonNull(race); }
    public final Race getRace() { return race; }
    
    public final FeatureManager getFeaturesManager() { return feats; }
    
    public final HealthPoints getHealthPoints() { return feats.getHealthPoints(); }
    public final NormalStat getAttack() { return feats.getAttack(); }
    public final NormalStat getDefense() { return feats.getDefense(); }
    public final NormalStat getSpecialAttack() { return feats.getSpecialAttack(); }
    public final NormalStat getSpecialDefense() { return feats.getSpecialDefense(); }
    public final NormalStat getSpeed() { return feats.getSpeed(); }
    
    public final int getMaxHealthPoints() { return feats.getHealthPoints().getMaxHealthPoints(); }
    public final int getCurrentHealthPoints() { return feats.getHealthPoints().getCurrentHealthPoints(); }
    
    public final int getPercentageMaxHealthPoints(float percentage) { return (int) (getMaxHealthPoints() * Math.abs(percentage)); }
    public final int getPercentageCurrentHealthPoints(float percentage) { return (int) (getCurrentHealthPoints() * Math.abs(percentage)); }
    
    public final float getCurrentHealthPointsPercentage() { return (float) getCurrentHealthPoints() / getMaxHealthPoints(); }
    
    public final boolean isAlive() { return getCurrentHealthPoints() > 0; }
    public final boolean isFullHealth() { return getCurrentHealthPoints() >= getMaxHealthPoints(); }
    
    public final Stat getStat(StatId statId) { return feats.getStat(statId); }
    
    public final ExperienceManager getExperienceManager() { return exp; }
    
    public final Growth getGrowth() { return exp.getGrowth(); }
    public final int getLevel() { return exp.getLevel(); }
    public final int getExperience() { return exp.getExperience(); }
    public final int getNextLevelExperience() { return exp.getNextLevelExperience(); }
    
    public final CreatureClass getCreatureClass() { return Formula.creatureClass(this); }
    
    public final AttackManager getAttackManager() { return attacks; }
    public final Attack getAttack(AttackSlot slot) { return attacks.getAttack(slot); }
    public final SelectedAttack selectAttackByAI(FighterTurnState state, AIIntelligence intel) { return attacks.selectAttackByAI(state, intel); }
    public final void setDefaultLearnedAttacksInLevel(int level) { attacks.setDefaultLearnedAttacksInLevel(race, level); }
    public final void setDefaultLearnedAttacks() { attacks.setDefaultLearnedAttacksInLevel(race, getLevel()); }
    
    public final PercentageFeature getAccuracy() { return state.getAccuracy(); }
    public final PercentageFeature getEvasion() { return state.getEvasion(); }
    
    public final void setCriticalHitBonus(boolean flag) { state.setCriticalHitBonus(flag); }
    public final boolean isCriticalHitBonus() { return state.isCriticalHitBonus(); }
    
    public final void setResting(boolean flag) { state.setResting(flag); }
    public final boolean isResting() { return state.isResting(); }
    
    public final void setIntimidated(boolean flag) { state.setIntimidated(flag); }
    public final boolean isIntimidated() { return state.isIntimidated(); }
    
    public final AlteredStateManager getAlterationManager() { return altered; }
    
    public final boolean isAlterationEnabled(AlteredStateId state) { return altered.isAlteredStateEnabled(state); }
    public final void addAlteration(RNG rng, BattleCommandManager bcm, AlteredState alteredState)
    {
        altered.addAlteredState(this, rng, bcm, alteredState);
    }
    public final void removeAlteration(RNG rng, BattleCommandManager bcm, AlteredStateId alteredState)
    {
        altered.removeAlteredState(this, rng, bcm, alteredState);
    }
    
    public final boolean isConfused() { return altered.isAlteredStateEnabled(AlteredStateId.CONFUSION); }
    public final boolean isParalyzed() { return altered.isAlteredStateEnabled(AlteredStateId.PARALYSIS); }
    public final boolean isBurned() { return altered.isAlteredStateEnabled(AlteredStateId.BURN); }
    public final boolean isPoisoned() { return altered.isAlteredStateEnabled(AlteredStateId.POISONING); }
    public final boolean isIntoxicated() { return altered.isAlteredStateEnabled(AlteredStateId.INTOXICATION); }
    public final boolean isSleeping() { return altered.isAlteredStateEnabled(AlteredStateId.SLEEP); }
    public final boolean isSleepiness() { return altered.isAlteredStateEnabled(AlteredStateId.SLEEPINESS); }
    public final boolean isFrozen() { return altered.isAlteredStateEnabled(AlteredStateId.FREEZING); }
    public final boolean isCursed() { return altered.isAlteredStateEnabled(AlteredStateId.CURSE); }
    public final boolean isWithNightmares() { return altered.isAlteredStateEnabled(AlteredStateId.NIGHTMARE); }
    
    
    public final ElementalManager getElementalTypeManager() { return types; }
    public final ElementalType getPrimaryType() { return types.getPrimaryType(); }
    public final ElementalType getSecondaryType() { return types.getSecondaryType(); }
    public final List<ElementalType> getAllTypes() { return types.getAllTypes(); }
    public final void replaceAllTypesBy(ElementalType type) { types.replaceTypesBy(type); }
    public final void addTemporaryType(ElementalType type) { types.addTemporaryType(type); }
    public final boolean hasType(ElementalType type) { return types.has(type); }
    public final boolean hasAnyType(ElementalType... types) { return this.types.has(types); }
    public final Effectivity effectivity(ElementalType type) { return types.effectivity(type); }
    public final String getElementalTypeNames() { return types.getElementalTypeNames(); }
    
    
    public final void setFighterId(TeamId id) { this.fighterId = id; }
    public final TeamId getFighterId() { return fighterId; }
    
    
    public final void setRemainingAbilityPoints(int points)
    {
        int current = feats.getAbilityPointsCount();
        this.remainingAbPoints = Utils.range(0, Formula.MAX_ABILITY_POINTS, points);
        if(current + remainingAbPoints > Formula.MAX_ABILITY_POINTS)
            remainingAbPoints = Formula.MAX_ABILITY_POINTS - current;
    }
    public final void giveAbilityPoints(int points) { setRemainingAbilityPoints(remainingAbPoints + points); }
    public final int getRemainingAbilityPoints() { return remainingAbPoints; }
    public final boolean canGiveAbilityPoints() { return !feats.hasMaxAbilityPoints(); }
    public final void useAbilityPoint(StatId statId)
    {
        if(remainingAbPoints > 0)
        {
            var stat = feats.getStat(statId);
            if(stat.getAbilityPoints() < Formula.MAX_STAT_ABILITY_POINTS)
            {
                remainingAbPoints--;
                stat.addAbilityPoints(1);
            }
        }
    }
    
    
    
    public final void updateAll()
    {
        feats.update(race, exp.getLevel(), nature);
    }
    
    public final void clearAll()
    {
        feats.clearAllAlterations();
        state.clearAllStates();
        altered.clearAllAlterations();
        types.restore(race);
        updateAll();
        feats.getHealthPoints().fullHeal();
    }
    
    public final void clearTransient()
    {
        feats.clearAllAlterations();
        state.clearAllStates();
        altered.clearTransientAlterations();
        updateAll();
    }
    
    
    public static final Creature createWild(Race race, int level, RNG rng)
    {
        var c = create(race, level);
        c.setName(race.getName());
        c.feats.randomizeStatGenetics(rng);
        c.attacks.setDefaultLearnedAttacksInLevel(race, level);
        c.clearAll();
        return c;
    }
    public static final Creature createWild(int raceId, int level, RNG rng)
    {
        return createWild(RacePool.getRace(raceId), level, rng);
    }
    
    
    public final boolean equals(Creature c)
    {
        return id.equals(c.id);
    }
    
    @Override
    public final boolean equals(Object o)
    {
        return o instanceof Creature &&
                id.equals(((Creature) o).id);
    }

    @Override
    public final int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public final int compareTo(Creature o)
    {
        var n0 = name == null ? race.getName() : name;
        var n1 = o.name == null ? o.race.getName() : o.name;
        return n0.compareTo(n1);
    }
    
    @Override
    public final String toString() { return name == null || name.isBlank() ? race.getName() : name; }
    
    
    
    
    private static Creature injector()
    {
        return new Creature();
    }
    
    private void afterInject()
    {
        if(id == null)
            id = UUID.randomUUID();
        clearAll();
    }
}
