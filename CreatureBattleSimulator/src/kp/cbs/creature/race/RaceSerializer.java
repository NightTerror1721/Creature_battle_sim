/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public class RaceSerializer extends AutowiredSerializer<RaceReference>
{
    public RaceSerializer() { super(RaceReference.class); }

    @Override
    public final UDLValue serialize(RaceReference value)
    {
        return UDLValue.valueOf(value.getRace().name);
    }

    @Override
    public final RaceReference unserialize(UDLValue value)
    {
        return new RaceReference(Race.getRace(value.getString()));
    }
    
}
