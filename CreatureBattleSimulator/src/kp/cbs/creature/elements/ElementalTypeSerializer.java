/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.elements;

import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class ElementalTypeSerializer extends AutowiredSerializer<ElementalType>
{
    public ElementalTypeSerializer() { super(ElementalType.class); }
    
    @Override
    public final UDLValue serialize(ElementalType value)
    {
        return UDLValue.valueOf(value.getId());
    }

    @Override
    public final ElementalType unserialize(UDLValue value)
    {
        return ElementalType.getElementalType(value.getInt());
    }
    
}
