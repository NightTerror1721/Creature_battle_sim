/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import kp.cbs.creature.Creature;
import kp.cbs.utils.Pair;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.autowired.Property;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class PlayerGame
{
    private String name = "default_slot";
    
    @Property private LinkedHashSet<Creature> creatures = new LinkedHashSet<>();
    @Property private HashSet<String> passedIds = new HashSet<>();
    @Property private HashMap<String, Integer> leageElos = new HashMap<>();
    @Property private HashMap<ItemId, Integer> items = new HashMap<>();
    
    public final void setName(String name) { this.name = Objects.requireNonNullElse(name, "default_slot"); }
    public final String getName() { return name; }
    
    public final void addCreature(Creature creature)
    {
        creatures.add(Objects.requireNonNull(creature));
    }
    public final int getCreatureCount() { return creatures.size(); }
    public final List<Creature> getAllCreatures()
    {
        return new ArrayList<>(creatures);
    }
    public final Stream<Creature> creatureStream() { return creatures.stream(); }
    public final void removeCreature(Creature creature) { creatures.remove(creature); }
    
    public final void addPassedId(String id) { passedIds.add(Objects.requireNonNull(id)); }
    public final boolean isIdPassed(String id) { return passedIds.contains(Objects.requireNonNull(id)); }
    public final boolean isIdsPassed(String[] ids) { return passedIds.containsAll(Arrays.asList(ids)); }
    public final boolean isIdsPassed(Collection<String> ids) { return passedIds.containsAll(ids); }
    
    public final void setLeageElo(String leageId, int elo) { leageElos.put(Objects.requireNonNull(leageId), Math.max(0, elo)); }
    public final int getLeageElo(String leageId) { return Math.max(0, leageElos.getOrDefault(leageId, 0)); }
    
    public final void addItemAmount(ItemId item, int amount)
    {
        var old = getItemAmount(item);
        items.put(item, Math.max(0, old + Math.max(0, amount)));
    }
    public final void removeItemAmount(ItemId item, int amount)
    {
        var old = getItemAmount(item);
        items.put(item, Math.max(0, old - Math.max(0, amount)));
    }
    public final int getItemAmount(ItemId item)
    {
        return Math.max(0, items.getOrDefault(item, 0));
    }
    public final boolean isItemAvailable(ItemId item) { return getItemAmount(item) > 0; }
    public final ItemId[] getAllAvailableItems()
    {
        return Stream.of(ItemId.values()).filter(this::isItemAvailable).toArray(ItemId[]::new);
    }
    public final Stream<Pair<ItemId, Integer>> streamAvailableItems()
    {
        return Stream.of(ItemId.values())
                .filter(this::isItemAvailable)
                .map(i -> new Pair<>(i, getItemAmount(i)));
    }
    
    
    
    
    public static final void save(PlayerGame game)
    {
        var path = Paths.concat(Paths.SAVES, game.name + ".sav");
        try
        {
            var base = Serializer.extract(game);
            Serializer.write(base, path);
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
    public static final PlayerGame load(String name)
    {
        var path = Paths.concat(Paths.SAVES, name + ".sav");
        try
        {
            if(!Files.isReadable(path))
                return new PlayerGame();
            var base = Serializer.read(path);
            var game = Serializer.inject(base, PlayerGame.class);
            game.setName(name);
            return game;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return new PlayerGame();
        }
    }
    
    public static final String[] getAllSavedNames()
    {
        try
        {
            return Files.list(Paths.SAVES)
                    .map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(".sav"))
                    .map(n -> n.substring(0, n.length() - 4))
                    .sorted(String::compareTo)
                    .toArray(String[]::new);
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return new String[] {};
        }
    }
}
