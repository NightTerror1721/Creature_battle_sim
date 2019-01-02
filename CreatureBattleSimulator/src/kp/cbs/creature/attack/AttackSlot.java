/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 *
 * @author Asus
 */
public enum AttackSlot
{
    SLOT_1,
    SLOT_2,
    SLOT_3,
    SLOT_4;
    
    private static final AttackSlot[] VALUES = values();
    
    public final AttackSlot next()
    {
        var nextId = ordinal() + 1;
        return nextId >= VALUES.length ? null : VALUES[nextId];
    }
    
    public final AttackSlot previous()
    {
        var nextId = ordinal() - 1;
        return nextId < 0 ? null : VALUES[nextId];
    }
    
    public final boolean hasNext() { return ordinal() < VALUES.length; }
    public final boolean hasPrevious() { return ordinal() >= 0; }
    
    public static final AttackSlot fromOrdinal(int ordinal)
    {
        return ordinal < 0 || ordinal >= VALUES.length ? null : VALUES[ordinal];
    }
    
    public static final Iterator<AttackSlot> iterator()
    {
        return new Iterator<>()
        {
            private AttackSlot slot = SLOT_1;
            
            @Override
            public final boolean hasNext() { return slot != null && slot.hasNext(); }

            @Override
            public final AttackSlot next()
            {
                var ret = slot;
                slot = slot.next();
                return ret;
            }
        };
    }
    
    public static final Iterable<AttackSlot> iterable() { return AttackSlot::iterator; }
    
    public static final Stream<AttackSlot> stream() { return Stream.of(values()); }
}
