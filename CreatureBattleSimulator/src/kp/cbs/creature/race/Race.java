/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kp.cbs.creature.Growth;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.StatId;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "afterInject")
public final class Race
{
    @Property(set = "__setName")
    String name;
    
    
    @Property(name = "hp_base", set = "__setHpBase")
    int hpBase;
    
    @Property(name = "attack_base", set = "__setAttBase")
    int attBase;
    
    @Property(name = "defense_base", set = "__setDefBase")
    int defBase;
    
    @Property(name = "spAttack_base", set = "__setSpAttBase")
    int spAttBase;
    
    @Property(name = "spDefense_base", set = "__setSpDefBase")
    int spDefBase;
    
    @Property(name = "speed_base", set = "__setSpdBase")
    int spdBase;
    
    
    @Property(name = "primary_type", set = "__setType1")
    ElementalType type1;
    
    @Property(name = "secondary_type", set = "__setType2")
    ElementalType type2;
    
    
    @Property(set = "__setGrowth")
    Growth growth;
    
    
    Race() {}
    
    final void __setName(String name) { this.name = Objects.requireNonNull(name); }
    
    final void __setHpBase(int value) { this.hpBase = Math.max(1, value); }
    final void __setAttBase(int value) { this.attBase = Math.max(1, value); }
    final void __setDefBase(int value) { this.defBase = Math.max(1, value); }
    final void __setSpAttBase(int value) { this.spAttBase = Math.max(1, value); }
    final void __setSpDefBase(int value) { this.spDefBase = Math.max(1, value); }
    final void __setSpdBase(int value) { this.spdBase = Math.max(1, value); }
    
    final void __setType1(ElementalType type) { this.type1 = Objects.requireNonNull(type); }
    final void __setType2(ElementalType type) { this.type2 = Objects.requireNonNull(type); }
    
    final void __setGrowth(Growth growth) { this.growth = Objects.requireNonNull(growth); }
    
    
    
    
    public final String getName() { return name; }
    
    public final int getHealthPointsBase() { return hpBase; }
    public final int getAttackBase() { return attBase; }
    public final int getDefenseBase() { return defBase; }
    public final int getSpecialAttackBase() { return spAttBase; }
    public final int getSpecialDefenseBase() { return spDefBase; }
    public final int getSpeedBase() { return spdBase; }
    
    public final int getBase(StatId stat)
    {
        switch(stat)
        {
            case HEALTH_POINTS: return hpBase;
            case ATTACK: return attBase;
            case DEFENSE: return defBase;
            case SPECIAL_ATTACK: return spAttBase;
            case SPECIAL_DEFENSE: return spDefBase;
            case SPEED: return spdBase;
            default: throw new IllegalStateException();
        }
    }
    
    public final ElementalType getPrimaryType() { return type1; }
    public final ElementalType getSecondaryType() { return type2; }
    
    public final Growth getGrowth() { return growth; }
    
    
    private static Race injector() { return new Race(); }
    
    private void afterInject()
    {
        
    }
    
    
    
    
    /* Static operations */
    public static final List<Race> getAllRaces() { return Collections.unmodifiableList(RaceManager.RACES.list); }
    
    public static final Race getRace(String name) { return RaceManager.RACES.races.getOrDefault(name, null); }
}
