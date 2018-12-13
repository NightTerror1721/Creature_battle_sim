/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class RaceAttackPool
{
    public static final class RaceAttack implements Comparable<RaceAttack>
    {
        @Property(set = "setAttackModel",
                  get = "getAttackModel",
                  customInjector = "loadModel",
                  customExtractor = "storeModel")
        private AttackModel attack;
        
        @Property(set = "setLevel")
        private int level;
        
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
    }
}
