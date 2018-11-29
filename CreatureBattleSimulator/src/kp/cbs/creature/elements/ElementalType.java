/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import kp.cbs.utils.Serializer;
import kp.cbs.utils.Utils;
import kp.udl.data.UDLValue;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class ElementalType
{
    private final int id;
    private final String name;
    private final HashMap<Integer, Effectivity> effectivity = new HashMap<>();
    
    private ElementalType(int id, UDLValue base)
    {
        this.id = id;
        this.name = base.getString("name");
        
        initEffectivities(base, "weak", Effectivity.SUPER_EFFECTIVE);
        initEffectivities(base, "resistant", Effectivity.NOT_VERY_EFFECTIVE);
        initEffectivities(base, "immune", Effectivity.NOT_EFFECTIVE);
    }
    private ElementalType() 
    {
        this.id = -1;
        this.name = "Desconocido";
    }
    
    private void initEffectivities(UDLValue base, String name, Effectivity effectivity)
    {
        for(UDLValue value : base.getList(name))
            this.effectivity.put(value.getInt(), effectivity);
    }
    
    public final int getId() { return id; }
    
    public final String getName() { return name; }
    
    public final Effectivity getEffectivity(ElementalType attackType)
    {
        return effectivity.getOrDefault(attackType.id, Effectivity.NORMAL_EFFECTIVE);
    }
    
    public final boolean equals(ElementalType et) { return id == et.id; }
    
    @Override
    public final boolean equals(Object o)
    {
        return o instanceof ElementalType &&
                id == ((ElementalType) o).id;
    }

    @Override
    public final int hashCode()
    {
        int hash = 5;
        hash = 67 * hash + this.id;
        return hash;
    }
    
    @Override
    public final String toString() { return name; }
    
    public static final Iterator<ElementalType> allIterator()
    {
        return new Iterator<ElementalType>()
        {
            private int it = 0;
            @Override public boolean hasNext() { return it < ALL.length; }
            @Override public ElementalType next() { return ALL[it++]; }
            
        };
    }
    public static final Iterable<ElementalType> allIterable() { return ElementalType::allIterator; }
    
    public static final ElementalType getElementalType(int id)
    {
        return id < 0 || id >= ALL.length ? UNKNOWN : ALL[id];
    }
    
    public static final int getElementalTypeCount() { return ALL.length; }
    
    
    
    
    private static final ElementalType[] ALL;
    public static final ElementalType NORMAL;
    public static final ElementalType FIGHT;
    public static final ElementalType PHYCHIC;
    public static final ElementalType MAGICAL;
    public static final ElementalType GHOST;
    public static final ElementalType DARK;
    public static final ElementalType FIRE;
    public static final ElementalType ICE;
    public static final ElementalType WATER;
    public static final ElementalType GROUND;
    public static final ElementalType ROCK;
    public static final ElementalType NATURE;
    public static final ElementalType VIENTO;
    public static final ElementalType ELECTRIC;
    public static final ElementalType STEEL;
    public static final ElementalType POISON;
    public static final ElementalType DRAGON;
    public static final ElementalType DEMON;
    public static final ElementalType DIVINE;
    public static final ElementalType UNKNOWN = new ElementalType();
    
    private static ElementalType create(UDLValue base, int id)
    {
        UDLValue value = base.get(id);
        if(value == null)
            throw new UDLException("Expected valid id in ElementalType");
        return ALL[id] = new ElementalType(id, value);
    }
    
    static
    {
        UDLValue base = null;
        try
        {
            base = Serializer.read(Utils.getInnerInputStream("/kp/cbs/config/Elements.udl"));
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
        
        ALL = new ElementalType[base.size() + 1];
        ALL[ALL.length - 1] = UNKNOWN;
        
        NORMAL = create(base, 0);
        FIGHT = create(base, 1);
        PHYCHIC = create(base, 2);
        MAGICAL = create(base, 3);
        GHOST = create(base, 4);
        DARK = create(base, 5);
        FIRE = create(base, 6);
        ICE = create(base, 7);
        WATER = create(base, 8);
        GROUND = create(base, 9);
        ROCK = create(base, 10);
        NATURE = create(base, 11);
        VIENTO = create(base, 12);
        ELECTRIC = create(base, 13);
        STEEL = create(base, 14);
        POISON = create(base, 15);
        DRAGON = create(base, 16);
        DEMON = create(base, 17);
        DIVINE = create(base, 18);
    }
}
