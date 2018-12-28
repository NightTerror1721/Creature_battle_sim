/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import kp.cbs.creature.race.Race;

/**
 *
 * @author Asus
 */
public final class ElementalManager
{
    private final ArrayList<ElementalType> elems = new ArrayList<>(3);
    
    public final void restore(Race race)
    {
        ElementalType primary;
        elems.clear();
        elems.add(primary = race.getPrimaryType());
        if(primary != race.getSecondaryType())
            elems.add(race.getSecondaryType());
    }
    
    public final ElementalType getPrimaryType() { return elems.get(0); }
    public final ElementalType getSecondaryType() { return elems.size() < 2 ? null : elems.get(1); }
    public final List<ElementalType> getAllTypes() { return Collections.unmodifiableList(elems); }
    
    public final void replaceTypesBy(ElementalType type)
    {
        elems.clear();
        addTemporaryType(type);
    }
    
    public final void addTemporaryType(ElementalType type)
    {
        elems.add(Objects.requireNonNull(type));
    }
    
    public final boolean has(ElementalType type) { return elems.contains(type); }
    public final boolean has(ElementalType... types)
    {
        for(ElementalType type : elems)
            for(int i=0;i<types.length;i++)
                if(type.equals(types[i]))
                    return true;
        return false;
    }
    
    public final Effectivity effectivity(ElementalType type)
    {
        Effectivity eff = Effectivity.NORMAL_EFFECTIVE;
        for(ElementalType etype : elems)
            eff = eff.combine(etype.getEffectivity(type));
        return eff;
    }
    
    public final String getElementalTypeNames()
    {
        var joiner = new StringJoiner(" ");
        for(var type : elems)
            joiner.add(type.getName());
        return joiner.toString();
    }
}
