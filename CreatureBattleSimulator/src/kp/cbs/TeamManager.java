/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kp.cbs.creature.Creature;

/**
 *
 * @author Asus
 */
public final class TeamManager implements Iterable<Creature>
{
    private final PlayerGame game;
    
    public TeamManager(PlayerGame game)
    {
        this.game = Objects.requireNonNull(game);
        
        if(getCreatureCount() < 1)
            setCreature(TeamSlot.SLOT_1, game.getAllCreatures().get(0));
    }
    
    public final void setCreature(TeamSlot slot, Creature creature)
    {
        if(creature != null && stream().noneMatch(c -> c.equals(creature)))
            game.setCreatureInSlot(slot, creature);
    }
    
    public final Creature getCreature(TeamSlot slot)
    {
        return game.getCreatureInSlot(slot);
    }
    
    public final void removeCreature(TeamSlot slot)
    {
        if(getCreatureCount() > 1)
            game.removeCreatureInSlot(slot);
    }
    
    public final int getCreatureCount() { return (int) stream().count(); }
    
    public final Stream<Creature> streamAvailableCreatures()
    {
        var used = stream().collect(Collectors.toSet());
        return game.getAllCreatures().stream()
                .filter(c -> !used.contains(c))
                .sorted((c0, c1) -> c0.getName().compareTo(c1.getName()));
    }
    public final Creature[] getArrayAvailableCreatures()
    {
        return streamAvailableCreatures().toArray(Creature[]::new);
    }
    public final List<Creature> getAvailableCreatures()
    {
        return streamAvailableCreatures().collect(Collectors.toList());
    }
    
    public final Creature[] getTeamCreatures()
    {
        return stream().filter(Creature::isAlive).toArray(Creature[]::new);
    }
    
    public final boolean isAnyAlive()
    {
        return stream().anyMatch(Creature::isAlive);
    }
    
    public final void clearTransient()
    {
        for(var creature : this)
            creature.clearTransient();
    }
    
    public final void clearAll()
    {
        for(var creature : this)
            creature.clearAll();
    }
    
    @Override
    public final Iterator<Creature> iterator()
    {
        return new Iterator<>()
        {
            private final TeamSlot[] slots = TeamSlot.values();
            private int it = -1;
            private Creature next = findNext();
            
            @Override
            public boolean hasNext() { return next != null; }

            @Override
            public Creature next()
            {
                if(next == null)
                    return null;
                var current = next;
                next = findNext();
                return current;
            }
            
            private Creature findNext()
            {
                for(it++; it < slots.length; it++)
                {
                    var c = game.getCreatureInSlot(slots[it]);
                    if(c != null)
                        return c;
                }
                return null;
            }
        };
    }
    
    public final Stream<Creature> stream()
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }
    
    public enum TeamSlot { SLOT_1, SLOT_2, SLOT_3, SLOT_4, SLOT_5, SLOT_6; }
}
