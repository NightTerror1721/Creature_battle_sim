/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.weather;

/**
 *
 * @author Asus
 */
public enum WeatherId
{
    RAIN("Lluvia"),
    INTENSE_SUN("Sol intenso"),
    SANDSTORM("Tormenta de arena"),
    HAIL("Granizada"),
    FOG("Niebla densa"),
    ELECTRIC_STORM("Tormenta eléctrica");
    
    private final String name;
    
    private WeatherId(String name) { this.name = name; }
    
    public final String getName() { return name; }
    
    @Override
    public final String toString() { return name; }
    
    private static final WeatherId[] VALUES = values();
    
    public static final int count() { return VALUES.length; }
    
    public static final WeatherId decode(int id) { return VALUES[id]; }
}
