/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        RaceAttack ra = new RaceAttack(level, attack);
        var list = hidden ? hiddenList : normalList;
        if(!list.contains(ra))
            list.add(ra);
    }
    
    public final void removeAttack(int level, AttackModel attack, boolean hidden)
    {
        RaceAttack ra = new RaceAttack(level, attack);
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
                .collect(Collectors.toUnmodifiableList());
    }
    
    public final List<AttackModel> getNormalAttacksInLevel(int level)
    {
        return normalList.stream()
                .filter(ra -> ra.level == level)
                .map(RaceAttack::getAttackModel)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public final Set<AttackModel> getAttacksUntilLevel(int level)
    {
        var list = Stream.concat(normalList.stream(), hiddenList.stream())
                .sorted(RaceAttack::compareTo)
                .filter(ra -> ra.level <= level)
                .map(RaceAttack::getAttackModel)
                .collect(Collectors.toSet());
        
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
        
        public RaceAttack() {}
        private RaceAttack(int level, AttackModel attack)
        {
            this.level = Utils.range(1, 100, level);
            this.attack = Objects.requireNonNull(attack);
        }
        
        public final void setLevel(int level) { this.level = Utils.range(1, 100, level); }
        public final int getLevel() { return level; }
        
        public final void setAttackModel(AttackModel attack) { this.attack = attack == null ? new AttackModel() : attack; }
        public final AttackModel getAttackModel() { return attack == null ? new AttackModel() : attack; }
        
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
    }
}
