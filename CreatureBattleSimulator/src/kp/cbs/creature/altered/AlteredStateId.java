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
    CONFUSION("Confusión", "CNF"),
    PARALYSIS("Parálisis", "PAR"),
    BURN("Quemadura", "QUE"),
    POISONING("Envenenamiento", "ENV"),
    INTOXICATION("Intoxicamiento", "ENVG"),
    SLEEP("Sueño", "DOR"),
    SLEEPINESS("Somnolencia", ""),
    FREEZING("Congelación", "CON"),
    CURSE("Maldición", "MAL"),
    NIGHTMARE("Pesadillas", "PES");
    
    private final String name;
    private final String abbreviation;
    
    private AlteredStateId(String name, String abbreviation)
    {
        this.name = name;
        this.abbreviation = abbreviation;
    }
    
    public final String getName() { return name; }
    
    public final String getAbbreviation() { return abbreviation; }
    
    @Override
    public final String toString() { return name; }
    
    private static final AlteredStateId[] VALUES = values();
    
    public static final int count() { return VALUES.length; }
    public static final AlteredStateId decode(int index)
    {
        return index < 0 || index >= VALUES.length ? CONFUSION : VALUES[index];
    }
}
