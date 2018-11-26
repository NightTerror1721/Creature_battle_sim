/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.util.HashMap;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class ElementalType
{
    /*
    00 NORMAL,
    01 FIGHT,
    02 PHYCHIC,
    03 MAGICAL,
    04 GHOST,
    05 DARK,
    06 FIRE,
    07 ICE,
    08 WATER,
    09 GROUND,
    10 ROCK,
    11 NATURE,
    12 VIENTO,
    13 ELECTRIC,
    14 STEEL,
    15 POISON,
    16 DRAGON,
    17 DEMON,
    18 DIVINE;*/
    
    private static final Float WEAK = 2f;
    private static final float NORMAL = 1f;
    private static final Float RESISTANT = 0.5f;
    private static final Float IMMUNE = 0f;
    
    private final int id;
    private final String name;
    private final HashMap<Integer, Float> effectivity = new HashMap<>();
    
    private ElementalType(UDLValue base)
    {
        this.id = base.getInt("id");
        this.name = base.getString("name");
        
        initEffectivities(base, "weak", WEAK);
        initEffectivities(base, "resistant", RESISTANT);
        initEffectivities(base, "immune", IMMUNE);
    }
    
    private void initEffectivities(UDLValue base, String name, Float effectivity)
    {
        for(UDLValue value : base.getList(name))
            this.effectivity.put(value.getInt(), effectivity);
    }
    
    public final String getName() { return name; }
    
    public final float getEffectivity(ElementalType attackType)
    {
        return effectivity.getOrDefault(attackType.id, NORMAL);
    }
    
    
}
