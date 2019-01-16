/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.util.Objects;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public class GlobalId implements Comparable<GlobalId>
{
    @Property(set = "setId", get = "getId")
    private String id = "";
    
    public final void setId(String id)
    {
        if(id.isBlank())
            throw new IllegalArgumentException();
        this.id = id;
    }
    
    public final String getId() { return id; }
    
    public final boolean equals(GlobalId obj)
    {
        return id.equals(obj.id);
    }
    
    @Override
    public final boolean equals(Object o)
    {
        return o instanceof GlobalId &&
                id.equals(((GlobalId) o).id);
    }

    @Override
    public final int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public final int compareTo(GlobalId o)
    {
        return id.compareTo(id);
    }
}
