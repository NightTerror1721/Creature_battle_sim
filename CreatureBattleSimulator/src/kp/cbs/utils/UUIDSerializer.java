/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.util.UUID;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class UUIDSerializer extends AutowiredSerializer<UUID>
{
    public UUIDSerializer() { super(UUID.class); }

    @Override
    public final UDLValue serialize(UUID value)
    {
        return UDLValue.valueOf(value.toString());
    }

    @Override
    public final UUID unserialize(UDLValue value)
    {
        try { return UUID.fromString(value.getString()); }
        catch(IllegalArgumentException ex) { return UUID.randomUUID(); }
    }
    
}
