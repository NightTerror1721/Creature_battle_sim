/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.util.ArrayList;
import java.util.Objects;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.feat.StatId;
import kp.cbs.utils.Serializer;
import kp.cbs.utils.Utils;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.autowired.Property;
import kp.udl.data.UDLObject;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class Evolution
{
    @Property
    private Race raceToEvolve;
    
    @Property
    private ArrayList<EvolveCondition> conditions = new ArrayList<>();
    
    
    public final void setRaceToEvolve(Race race) { this.raceToEvolve = Objects.requireNonNull(race); }
    public final Race getRaceToEvolve() { return raceToEvolve; }
    
    public final int getConditionCount() { return conditions.size(); }
    public final EvolveCondition getCondition(int index) { return conditions.get(index); }
    public final void addCondition(EvolveCondition condition) { this.conditions.add(Objects.requireNonNull(condition)); }
    public final void removeCondition(int index) { conditions.remove(index); }
    
    @Override
    public final String toString() { return raceToEvolve.getName(); }
    
    
    public static final AutowiredSerializer<EvolveCondition> EVO_COND_SERIALIZER = new AutowiredSerializer<>(EvolveCondition.class)
    {
        @Override
        public final UDLValue serialize(EvolveCondition value)
        {
            return new UDLObject()
                    .set("type", UDLValue.valueOf(value.getType().ordinal()))
                    .set("condition", Serializer.extract(value));
        }
        
        @Override
        public final EvolveCondition unserialize(UDLValue value)
        {
            var type = EvolutionConditionType.decode(value.getInt("type"));
            switch(type)
            {
                case LEVEL: return Serializer.inject(value.get("condition"), LevelEvolveCondition.class);
                case STAT_GREATER_THAN: return Serializer.inject(value.get("condition"), GreaterStatEvolveCondition.class);
                case STAT_EQUALS_TO: return Serializer.inject(value.get("condition"), EqualsStatEvolveCondition.class);
                case LEARNED_ATTACK: return Serializer.inject(value.get("condition"), LearnedAttackEvolveCondition.class);
                default: return new LevelEvolveCondition();
            }
        }
    };
    
    public static abstract class EvolveCondition
    {
        public abstract EvolutionConditionType getType();
        
        public abstract boolean check(Creature creature);
    }
    
    public static final class LevelEvolveCondition extends EvolveCondition
    {
        @Property
        private int level = 1;
        
        public final void setLevel(int level) { this.level = Utils.range(1, 100, level); }
        public final int getLevel() { return level; }
        
        @Override
        public final EvolutionConditionType getType() { return EvolutionConditionType.LEVEL; }

        @Override
        public final boolean check(Creature creature)
        {
            return creature.getLevel() >= level;
        }
        
        @Override
        public final String toString() { return "Nivel >= " + level; }
    }
    
    public static abstract class StatEvolveCondition extends EvolveCondition
    {
        @Property
        StatId stat0 = StatId.ATTACK;
        
        @Property
        StatId stat1 = StatId.DEFENSE;
        
        private StatEvolveCondition() {}
        
        public final void setFirstStat(StatId stat) { this.stat0 = Objects.requireNonNull(stat); }
        public final void setSecondStat(StatId stat) { this.stat1 = Objects.requireNonNull(stat); }
        
        public final StatId getFirstStat() { return stat0; }
        public final StatId getSecondStat() { return stat1; }
    }
    
    public static final class GreaterStatEvolveCondition extends StatEvolveCondition
    {
        @Override
        public final EvolutionConditionType getType() { return EvolutionConditionType.STAT_GREATER_THAN; }

        @Override
        public final boolean check(Creature creature) { return creature.getStat(stat0).getValue() > creature.getStat(stat1).getValue(); }
        
        @Override
        public final String toString() { return stat0 + " > " + stat1; }
    }
    
    public static final class EqualsStatEvolveCondition extends StatEvolveCondition
    {
        @Override
        public final EvolutionConditionType getType() { return EvolutionConditionType.STAT_EQUALS_TO; }

        @Override
        public final boolean check(Creature creature) { return creature.getStat(stat0).getValue() == creature.getStat(stat1).getValue(); }
        
        @Override
        public final String toString() { return stat0 + " = " + stat1; }
    }
    
    public static final class LearnedAttackEvolveCondition extends EvolveCondition
    {
        @Property
        private AttackModel attack;
        
        public final void setAttack(AttackModel attack) { this.attack = attack; } 
        public final AttackModel getAttack() { return attack; }
        
        @Override
        public final EvolutionConditionType getType() { return EvolutionConditionType.LEARNED_ATTACK; }

        @Override
        public final boolean check(Creature creature)
        {
            return attack != null && creature.getAttackManager().stream().anyMatch(a -> a.getModel().equals(attack));
        }
        
        @Override
        public final String toString() { return "Debe conocer: " + attack.getName(); }
    }
    
    public enum EvolutionConditionType
    {
        LEVEL, STAT_GREATER_THAN, STAT_EQUALS_TO, LEARNED_ATTACK;
        
        public static final EvolutionConditionType decode(int ord)
        {
            switch(ord)
            {
                default:
                case 0: return LEVEL;
                case 1: return STAT_GREATER_THAN;
                case 2: return STAT_EQUALS_TO;
                case 3: return LEARNED_ATTACK;
            }
        }
    }
}
