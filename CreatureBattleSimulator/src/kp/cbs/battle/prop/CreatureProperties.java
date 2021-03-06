/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.prop;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import kp.cbs.creature.Creature;
import kp.cbs.creature.Nature;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackSlot;
import kp.cbs.creature.attack.RaceAttackPool.RaceAttack;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.race.Race;
import kp.cbs.utils.RNG;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class CreatureProperties
{
    @Property private Race race;
    
    @Property private String name = " ";
    
    @Property private int minLevel = 1;
    
    @Property private int maxLevel = 1;
    
    @Property private Nature nature = Nature.UNDEFINED;
    
    
    @Property private boolean randomGenetic = true;
    
    @Property private int hpGen;
    @Property private int attackGen;
    @Property private int defenseGen;
    @Property private int spAttackGen;
    @Property private int spDefenseGen;
    @Property private int speedGen;
    
    
    @Property private boolean levelAttacks = true;
    
    @Property private int maxHiddenAttacks;
    
    @Property private AttackModel att1;
    @Property private AttackModel att2;
    @Property private AttackModel att3;
    @Property private AttackModel att4;
    
    
    @Property private int uniqueAb = 0;
    
    @Property private int hpAb;
    @Property private int attackAb;
    @Property private int defenseAb;
    @Property private int spAttackAb;
    @Property private int spDefenseAb;
    @Property private int speedAb;
    
    

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = Objects.requireNonNull(race);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = Utils.range(1, 100, minLevel);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = Utils.range(1, 100, maxLevel);
    }
    
    public void setNature(Nature nature) {
        this.nature = Objects.requireNonNullElse(nature, Nature.UNDEFINED);
    }
    
    public Nature getNature() {
        return nature;
    }

    public boolean isRandomGenetic() {
        return randomGenetic;
    }

    public void setRandomGenetic(boolean randomGenetic) {
        this.randomGenetic = randomGenetic;
    }

    public int getHpGenetic() {
        return hpGen;
    }

    public void setHpGenetic(int hpGen) {
        this.hpGen = hpGen;
    }

    public int getAttackGenetic() {
        return attackGen;
    }

    public void setAttackGenetic(int attackGen) {
        this.attackGen = attackGen;
    }

    public int getDefenseGenetic() {
        return defenseGen;
    }

    public void setDefenseGenetic(int defenseGen) {
        this.defenseGen = defenseGen;
    }

    public int getSpecialAttackGenetic() {
        return spAttackGen;
    }

    public void setSpecialAttackGenetic(int spAttackGen) {
        this.spAttackGen = spAttackGen;
    }

    public int getSpecialDefenseGenetic() {
        return spDefenseGen;
    }

    public void setSpecialDefenseGenetic(int spDefenseGen) {
        this.spDefenseGen = spDefenseGen;
    }

    public int getSpeedGenetic() {
        return speedGen;
    }

    public void setSpeedGenetic(int speedGen) {
        this.speedGen = speedGen;
    }

    public boolean isLevelAttacks() {
        return levelAttacks;
    }

    public void setLevelAttacks(boolean levelAttacks) {
        this.levelAttacks = levelAttacks;
    }
    
    public final int getMaxHiddenAttacks() { return maxHiddenAttacks; }
    public final void setMaxHiddenAttacks(int number) { this.maxHiddenAttacks = Utils.range(0, 4, number); }

    public AttackModel getAttack(AttackSlot slot) {
        switch(slot)
        {
            default: throw new IllegalStateException();
            case SLOT_1: return att1;
            case SLOT_2: return att2;
            case SLOT_3: return att3;
            case SLOT_4: return att4;
        }
    }
    public RaceAttack getRaceAttack(AttackSlot slot) {
        if(race == null)
            return null;
        var model = getAttack(slot);
        return race.getAttackPool().findRaceAttack(model);
    }

    public void setAttack(AttackSlot slot, AttackModel att) {
        switch(slot)
        {
            default: throw new IllegalStateException();
            case SLOT_1: this.att1 = att;
            case SLOT_2: this.att2 = att;
            case SLOT_3: this.att3 = att;
            case SLOT_4: this.att4 = att;
        }
    }
    
    public final boolean hasUniqueAbility() { return uniqueAb >= 0; }
    public final void dissableUniqueAbility() { uniqueAb = -1; }

    public int getUniqueAbilityValue() {
        return uniqueAb;
    }

    public void setUniqueAbilityValue(int points) {
        this.uniqueAb = Utils.range(0, 512, points);
    }

    public int getHpAbility() {
        return hpAb;
    }

    public void setHpAbility(int points) {
        this.hpAb = Utils.range(0, 512, points);
    }

    public int getAttackAbility() {
        return attackAb;
    }

    public void setAttackAbility(int points) {
        this.attackAb = Utils.range(0, 512, points);
    }

    public int getDefenseAbility() {
        return defenseAb;
    }

    public void setDefenseAbility(int points) {
        this.defenseAb = Utils.range(0, 512, points);
    }

    public int getSpecialAttackAbility() {
        return spAttackAb;
    }

    public void setSpecialAttackAbility(int points) {
        this.spAttackAb = Utils.range(0, 512, points);
    }

    public int getSpecialDefenseAbility() {
        return spDefenseAb;
    }

    public void setSpecialDefenseAbility(int points) {
        this.spDefenseAb = Utils.range(0, 512, points);
    }

    public int getSpeedAbility() {
        return speedAb;
    }

    public void setSpeedAbility(int points) {
        this.speedAb = Utils.range(0, 512, points);
    }
    
    
    private int selectLevel(RNG rng, int elo, int minElo, int maxElo)
    {
        if(minLevel == maxLevel)
            return Utils.range(1, 100, maxLevel);
        
        var min = Utils.range(1, 100, Math.min(minLevel, maxLevel));
        var max = Utils.range(1, 100, Math.max(minLevel, maxLevel));
        
        if(elo <= minElo)
            return min + rng.d2();
        if(elo >= maxElo)
            return max;
        
        int mid = min + ((int) ((max - min) * ((float) (elo - minElo) / (maxElo - minElo))));
        
        return Utils.range(min, max, mid + (rng.d3()));
    }
    public final Creature generateCreature(RNG rng, int currentElo, int minElo, int maxElo)
    {
        var creature = Creature.create(race, selectLevel(rng, currentElo, minElo, maxElo));
        return generateCreature(creature, rng);
    }
    
    
    private int selectLevel(RNG rng)
    {
        if(minLevel == maxLevel)
            return Utils.range(1, 100, maxLevel);
        
        var min = Utils.range(1, 100, Math.min(minLevel, maxLevel));
        var max = Utils.range(1, 100, Math.max(minLevel, maxLevel));
        
        return Utils.range(min, max, min + (rng.d(max - min + 1)));
    }
    public final Creature generateCreature(RNG rng)
    {
        var creature = Creature.create(race, selectLevel(rng));
        return generateCreature(creature, rng);
    }
    
    private Creature generateCreature(Creature creature, RNG rng)
    {
        creature.setName(name.isBlank() ? race.getName() : name);
        
        if(nature == null || nature == Nature.UNDEFINED)
            creature.setNature(Nature.random(rng));
        else creature.setNature(nature);
        
        if(isRandomGenetic())
            creature.getFeaturesManager().randomizeStatGenetics(rng);
        else
        {
            creature.getHealthPoints().setGeneticPoints(hpGen);
            creature.getAttack().setGeneticPoints(attackGen);
            creature.getDefense().setGeneticPoints(defenseGen);
            creature.getSpecialAttack().setGeneticPoints(spAttackGen);
            creature.getSpecialDefense().setGeneticPoints(spDefenseGen);
            creature.getSpeed().setGeneticPoints(speedGen);
        }
        
        if(hasUniqueAbility())
            for(StatId id : StatId.values())
                creature.getStat(id).setAbilityPoints(uniqueAb);
        else
        {
            creature.getHealthPoints().setAbilityPoints(hpAb);
            creature.getAttack().setAbilityPoints(attackAb);
            creature.getDefense().setAbilityPoints(defenseAb);
            creature.getSpecialAttack().setAbilityPoints(spAttackAb);
            creature.getSpecialDefense().setAbilityPoints(spDefenseAb);
            creature.getSpeed().setAbilityPoints(speedAb);
        }
        
        if(isLevelAttacks())
            fillLevelAttacks(creature, rng);
        else for(var slot : AttackSlot.iterable())
        {
            var att = getAttack(slot);
            if(att != null)
                creature.getAttackManager().setAttack(slot, att);
        }
        
        creature.clearAll();
        return creature;
    }
    
    private void fillLevelAttacks(Creature creature, RNG rng)
    {
        var hidden = Utils.range(0, 4, maxHiddenAttacks);
        if(hidden > 0)
            hidden = rng.d(hidden + 1);
        if(hidden > 0 && hidden <= 4)
        {
            var hiddenAtts = race.getAttackPool().getAttacksUntilLevel(true, creature.getLevel());
            var normalAtts = new LinkedList<>(Arrays.asList(race.getAttackPool().getDefaultLearnedInLevel(creature.getLevel())));
            var slot = AttackSlot.SLOT_1;
            
            while(slot != null && hidden > 0 && !hiddenAtts.isEmpty())
            {
                creature.getAttackManager().setAttack(slot, hiddenAtts.remove(rng.d(hiddenAtts.size())));
                slot = slot.next();
                hidden--;
            }
            
            while(slot != null && !normalAtts.isEmpty())
            {
                creature.getAttackManager().setAttack(slot, normalAtts.removeLast());
                slot = slot.next();
            }
        }
        else creature.getAttackManager().setDefaultLearnedAttacksInLevel(race, creature.getLevel());
    }
    
}
