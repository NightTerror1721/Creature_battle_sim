/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.util.Objects;

/**
 *
 * @author Asus
 */
public class RaceReference
{
    private final Race race;
    
    public RaceReference(Race race)
    {
        this.race = Objects.requireNonNull(race);
    }
    
    public final Race getRace() { return race; }
}
