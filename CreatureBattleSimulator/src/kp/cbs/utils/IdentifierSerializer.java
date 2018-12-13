/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public abstract class IdentifierSerializer<T extends IdentifierObject> extends AutowiredSerializer<T>
{
    public IdentifierSerializer(Class<T> jclass) { super(jclass); }

    @Override
    public UDLValue serialize(T value)
    {
        return UDLValue.valueOf(value.getId());
    }
}
