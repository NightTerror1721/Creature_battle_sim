/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.flag;

import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class FlagModel
{
    @Property(invalidEnumValue = "POWER")
    private FlagType type;
    
    @Property
    private int id;
    
    @Property
    private String name;
    
    public final FlagType getType() { return type; }
    public final int getId() { return id; }
    public final String getName() { return name; }
}
