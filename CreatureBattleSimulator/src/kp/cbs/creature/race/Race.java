/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.util.Objects;
import kp.cbs.creature.Growth;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.StatId;
import kp.cbs.utils.IdentifierObject;
import kp.cbs.utils.IdentifierSerializer;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.autowired.Property;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class Race implements IdentifierObject
{
    @Property(set = "setId")
    int id;
    
    @Property(set = "setName")
    String name;
    
    
    @Property(name = "hp_base", set = "setHpBase")
    int hpBase;
    
    @Property(name = "attack_base", set = "setAttBase")
    int attBase;
    
    @Property(name = "defense_base", set = "setDefBase")
    int defBase;
    
    @Property(name = "spAttack_base", set = "setSpAttBase")
    int spAttBase;
    
    @Property(name = "spDefense_base", set = "setSpDefBase")
    int spDefBase;
    
    @Property(name = "speed_base", set = "setSpdBase")
    int spdBase;
    
    
    @Property(name = "primary_type", set = "setPrimaryType")
    ElementalType type1;
    
    @Property(name = "secondary_type", set = "setSecondaryType")
    ElementalType type2;
    
    
    @Property(set = "setGrowth", invalidEnumValue = "NORMAL")
    Growth growth;
    
    
    @Override
    public final void setId(int id) { this.id = Math.max(0, id); }
    
    public final void setName(String name) { this.name = Objects.requireNonNull(name); }
    
    public final void setHpBase(int value) { this.hpBase = Math.max(1, value); }
    public final void setAttBase(int value) { this.attBase = Math.max(1, value); }
    public final void setDefBase(int value) { this.defBase = Math.max(1, value); }
    public final void setSpAttBase(int value) { this.spAttBase = Math.max(1, value); }
    public final void setSpDefBase(int value) { this.spDefBase = Math.max(1, value); }
    public final void setSpdBase(int value) { this.spdBase = Math.max(1, value); }
    
    public final void setPrimaryType(ElementalType type) { this.type1 = Objects.requireNonNull(type); }
    public final void setSecondaryType(ElementalType type) { this.type2 = Objects.requireNonNull(type); }
    
    public final void setGrowth(Growth growth) { this.growth = Objects.requireNonNull(growth); }
    
    
    
    
    @Override
    public final int getId() { return id; }
    
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
    
    
    
    public static final AutowiredSerializer<Race> SERIALIZER = new IdentifierSerializer<>(Race.class)
    {
        @Override
        public final Race unserialize(UDLValue value)
        {
            return RacePool.getRace(value.getInt());
        }
    };
}
