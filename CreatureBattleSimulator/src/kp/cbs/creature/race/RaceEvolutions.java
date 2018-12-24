/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.util.ArrayList;
import java.util.Objects;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class RaceEvolutions
{
    @Property
    private ArrayList<Evolution> evolutions = new ArrayList<>();
    
    public final int getEvolutionCount() { return evolutions.size(); }
    public final Evolution getEvolution(int index) { return evolutions.get(index); }
    public final void addEvolution(Evolution evo) { this.evolutions.add(Objects.requireNonNull(evo)); }
    public final void removeEvolution(int index) { evolutions.remove(index); }
}
