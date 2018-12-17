/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

/**
 *
 * @author Asus
 */
public enum AlteredStateId
{
    CONFUSION("Confusión"),
    PARALYSIS("Parálisis"),
    BURN("Quemadura"),
    POISONING("Envenenamiento"),
    INTOXICATION("Intoxicamiento"),
    SLEEP("Sueño"),
    SLEEPINESS("Somnolencia"),
    FREEZING("Congelación"),
    CURSE("Maldición"),
    NIGHTMARE("Pesadillas");
    
    public final String name;
    
    private AlteredStateId(String name) { this.name = name; }
    
    public final String getName() { return name; }
    
    @Override
    public final String toString() { return name; }
    
    private static final AlteredStateId[] VALUES = values();
    
    public static final int count() { return VALUES.length; }
    public static final AlteredStateId decode(int index)
    {
        return index < 0 || index >= VALUES.length ? CONFUSION : VALUES[index];
    }
}
