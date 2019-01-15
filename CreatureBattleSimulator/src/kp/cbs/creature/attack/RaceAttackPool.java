/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kp.cbs.utils.Utils;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
@InjectOptions(afterBuild = "sortByLevel")
public final class RaceAttackPool
{
    @Property private LinkedList<RaceAttack> normalList;
    @Property private LinkedList<RaceAttack> hiddenList;
    @Property private AttackModel innate;
    
    public static final RaceAttackPool instance()
    {
        RaceAttackPool pool = new RaceAttackPool();
        pool.normalList = new LinkedList<>();
        pool.hiddenList = new LinkedList<>();
        
        return pool;
    }
    
    public final void setInnateAttack(AttackModel attack)
    {
        this.innate = attack;
    }
    public final AttackModel getInnateAttack() { return innate == null ? new AttackModel() : innate; }
    
    public final void registerAttack(int level, AttackModel attack, boolean hidden)
    {
        RaceAttack ra = new RaceAttack(level, attack, hidden, false);
        var list = hidden ? hiddenList : normalList;
        if(!list.contains(ra))
            list.add(ra);
    }
    
    public final void removeAttack(int level, AttackModel attack, boolean hidden)
    {
        RaceAttack ra = new RaceAttack(level, attack, hidden, false);
        var list = hidden ? hiddenList : normalList;
        list.remove(ra);
    }
    
    public final void sortByLevel()
    {
        normalList.sort(RaceAttack::compareTo);
        hiddenList.sort(RaceAttack::compareTo);
    }
    
    public final List<AttackModel> getAllAttacks(boolean hidden)
    {
        var list = hidden ? hiddenList : normalList;
        return list.stream()
                .map(RaceAttack::getAttackModel)
                .collect(Collectors.toList());
    }
    
    public final Stream<RaceAttack> streamAllAttacks()
    {
        return Stream.concat(normalList.stream(), hiddenList.stream());
    }
    public final List<RaceAttack> getAllAttacks()
    {
        return streamAllAttacks().collect(Collectors.toList());
    }
    public final RaceAttack[] getAllAttacksInArray()
    {
        var stream = innate == null
                ? streamAllAttacks()
                : Stream.concat(Stream.of(new RaceAttack(1, innate, false, true)), streamAllAttacks());
        return stream.toArray(RaceAttack[]::new);
    }
    
    public final List<AttackModel> getNormalAttacksInLevel(int level, boolean includeInnate)
    {
        var list = normalList.stream()
                .filter(ra -> ra.level == level)
                .map(RaceAttack::getAttackModel)
                .collect(Collectors.toList());
        
        if(includeInnate && innate != null && !list.contains(innate))
            list.add(innate);
        return list;
    }
    
    public final List<AttackModel> getAttacksUntilLevel(boolean hidden, int level)
    {
        var list = (hidden ? hiddenList.stream() : normalList.stream())
                .filter(ra -> ra.level <= level)
                .sorted(RaceAttack::compareTo)
                .map(RaceAttack::getAttackModel)
                .collect(Collectors.toList());
        if(innate != null)
            list.add(innate);
        return list;
    }
    
    public final Stream<RaceAttack> streamRaceAttacksUntilLevel(int level)
    {
        var stream = innate == null
                ? streamAllAttacks()
                : Stream.concat(Stream.of(new RaceAttack(1, innate, false, true)), streamAllAttacks());
        return stream.filter(ra -> ra.level <= level);
    }
    
    public final AttackModel[] getDefaultLearnedInLevel(int level)
    {
        AttackModel[] array = normalList.stream()
                .filter(ra -> ra.level <= level)
                .sorted(RaceAttack::inverseCompareTo)
                .limit(4)
                .sorted(RaceAttack::compareTo)
                .map(RaceAttack::getAttackModel)
                .toArray(AttackModel[]::new);
        
        if(array.length == 4)
            return array;
        
        AttackModel[] result = new AttackModel[4];
        if(innate != null)
        {
            result[0] = innate;
            System.arraycopy(array, 0, result, 1, array.length > 3 ? 3 : array.length);
        }
        else System.arraycopy(array, 0, result, 0, array.length > 4 ? 4 : array.length);
        
        return result;
    }
    
    public final RaceAttack findRaceAttack(AttackModel model)
    {
        if(model == null || (innate != null && model.equals(innate)))
            return new RaceAttack(1, innate, false, true);
        return streamAllAttacks().filter(a -> a.equals(model)).findFirst().orElse(null);
    }
    
    
    public static final class RaceAttack implements Comparable<RaceAttack>
    {
        @Property(set = "setAttackModel",
                  get = "getAttackModel",
                  customInjector = "loadModel",
                  customExtractor = "storeModel")
        private AttackModel attack;
        
        @Property(set = "setLevel")
        private int level;
        
        @Property
        private boolean hidden;
        
        private boolean innate;
        
        public RaceAttack() {}
        private RaceAttack(int level, AttackModel attack, boolean hidden, boolean innate)
        {
            this.level = Utils.range(1, 100, level);
            this.attack = Objects.requireNonNull(attack);
            this.hidden = hidden;
            this.innate = innate;
        }
        
        public final void setLevel(int level) { this.level = Utils.range(1, 100, level); }
        public final int getLevel() { return level; }
        
        public final void setAttackModel(AttackModel attack) { this.attack = attack == null ? new AttackModel() : attack; }
        public final AttackModel getAttackModel() { return attack == null ? new AttackModel() : attack; }
        
        public final boolean isHidden() { return hidden; }
        
        public final boolean isInnate() { return innate; }
        
        private AttackModel loadModel(UDLValue value)
        {
            return AttackPool.getModel(value.getInt());
        }
        
        private UDLValue storeModel(AttackModel model)
        {
            return UDLValue.valueOf(model.getId());
        }

        @Override
        public final int compareTo(RaceAttack o)
        {
            return Integer.compare(level, o.level);
        }
        public final int inverseCompareTo(RaceAttack o)
        {
            return (level > o.level) ? -1 : ((level == o.level) ? 0 : 1);
        }
        
        @Override
        public final boolean equals(Object o)
        {
            if(o instanceof RaceAttack)
            {
                RaceAttack ra = (RaceAttack) o;
                return level == ra.level && attack.equals(ra.attack);
            }
            return false;
        }
        public final boolean equals(RaceAttack o) { return level == o.level && attack.equals(o.attack); }

        @Override
        public final int hashCode()
        {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.attack);
            hash = 53 * hash + this.level;
            return hash;
        }
        
        @Override
        public final String toString()
        {
            if(isInnate())
                return "[Innato]: " + attack.getName();
            return isHidden()
                    ? "[Oculto] " + level + ": " + attack.getName()
                    : level + ": " + attack.getName();
        }
    }
}
