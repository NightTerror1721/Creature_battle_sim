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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import kp.cbs.TeamManager.TeamSlot;
import kp.cbs.creature.Creature;
import kp.cbs.place.Leage;
import kp.cbs.place.Place;
import kp.cbs.utils.Pair;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
@InjectOptions(afterBuild = "check")
public final class PlayerGame
{
    private String name = "default_slot";
    
    @Property private LinkedHashMap<UUID, Creature> creatures = new LinkedHashMap<>();
    @Property private HashSet<String> passedIds = new HashSet<>();
    @Property private HashMap<String, Integer> leageElos = new HashMap<>();
    @Property private HashMap<ItemId, Integer> items = new HashMap<>();
    
    @Property private UUID slot1, slot2, slot3, slot4, slot5, slot6;
    
    @Property private int money;
    
    @Property private String currentPlace = "";
    @Property private String currentLeage = "";
    
    public final void setName(String name) { this.name = Objects.requireNonNullElse(name, "default_slot"); }
    public final String getName() { return name; }
    
    public final void addCreature(Creature creature)
    {
        creatures.put(creature.getId(), Objects.requireNonNull(creature));
    }
    public final int getCreatureCount() { return creatures.size(); }
    public final Creature getCreature(UUID id) { return creatures.getOrDefault(id, null); }
    public final List<Creature> getAllCreatures()
    {
        return new ArrayList<>(creatures.values());
    }
    public final Stream<Creature> creatureStream() { return creatures.values().stream(); }
    public final void removeCreature(Creature creature) { creatures.remove(creature.getId()); }
    public final void removeCreature(UUID creatureId) { creatures.remove(creatureId); }
    
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
    public final ItemId[] getAvailableCatchItems()
    {
        return Stream.of(ItemId.values())
                .filter(i -> i.isCatcherItem() && isItemAvailable(i))
                .toArray(ItemId[]::new);
    }
    
    public final UUID getCreatureIdInSlot(TeamSlot slot)
    {
        switch(slot)
        {
            case SLOT_1: return slot1;
            case SLOT_2: return slot2;
            case SLOT_3: return slot3;
            case SLOT_4: return slot4;
            case SLOT_5: return slot5;
            case SLOT_6: return slot6;
            default: return null;
        }
    }
    public final Creature getCreatureInSlot(TeamSlot slot)
    {
        UUID id;
        switch(slot)
        {
            case SLOT_1: id = slot1; break;
            case SLOT_2: id = slot2; break;
            case SLOT_3: id = slot3; break;
            case SLOT_4: id = slot4; break;
            case SLOT_5: id = slot5; break;
            case SLOT_6: id = slot6; break;
            default: return null;
        }
        
        if(id == null)
            return null;
        return getCreature(id);
    }
    public final boolean setCreatureInSlot(TeamSlot slot, Creature creature)
    {
        if(getCreature(creature.getId()) == null)
            return false;
        
        switch(slot)
        {
            case SLOT_1: slot1 = creature.getId(); return true;
            case SLOT_2: slot2 = creature.getId(); return true;
            case SLOT_3: slot3 = creature.getId(); return true;
            case SLOT_4: slot4 = creature.getId(); return true;
            case SLOT_5: slot5 = creature.getId(); return true;
            case SLOT_6: slot6 = creature.getId(); return true;
            default: return false;
        }
    }
    
    public final void removeCreatureInSlot(TeamSlot slot)
    {
        switch(slot)
        {
            case SLOT_1: slot1 = null; break;
            case SLOT_2: slot2 = null; break;
            case SLOT_3: slot3 = null; break;
            case SLOT_4: slot4 = null; break;
            case SLOT_5: slot5 = null; break;
            case SLOT_6: slot6 = null; break;
        }
    }
    
    public final void setMoney(int money) { this.money = Math.max(0, money); }
    public final int getMoney() { return money; }
    public final void useMoney(int amount) { setMoney(money - Math.max(0, amount)); }
    public final void addMoney(int amount) { setMoney(money + Math.max(0, amount)); }
    public final boolean hasEnoughMoney(int amount) { return money >= Math.max(0, amount); }
    
    public final void setCurrentPlace(String placeName)
    {
        this.currentPlace = placeName;
    }
    public final void setCurrentPlace(Place place) { setCurrentPlace(place.getName()); }
    public final String getCurrentPlace() { return currentPlace; }
    
    public final void setCurrentLeage(String leageName)
    {
        this.currentLeage = leageName;
    }
    public final void setCurrentLeage(Leage leage) { setCurrentLeage(leage.getName()); }
    public final String getCurrentLeage() { return currentLeage; }
    
    
    
    private void check()
    {
        checkSlot(TeamSlot.SLOT_1);
        checkSlot(TeamSlot.SLOT_2);
        checkSlot(TeamSlot.SLOT_3);
        checkSlot(TeamSlot.SLOT_4);
        checkSlot(TeamSlot.SLOT_5);
        checkSlot(TeamSlot.SLOT_6);
    }
    private void checkSlot(TeamSlot slot)
    {
        var id = getCreatureIdInSlot(slot);
        var creature = getCreatureInSlot(slot);
        if(id != null && creature == null)
            removeCreatureInSlot(slot);
    }
    
    
    
    
    
    public static final boolean save(PlayerGame game)
    {
        var path = Paths.concat(Paths.SAVES, game.name + ".sav");
        try
        {
            var base = Serializer.extract(game);
            Serializer.write(base, path);
            return true;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
    }
    
    public static final boolean exists(String name)
    {
        var path = Paths.concat(Paths.SAVES, name + ".sav");
        try
        {
            if(!Files.isReadable(path))
                return false;
            var base = Serializer.read(path);
            Serializer.inject(base, PlayerGame.class);
            return true;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return false;
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
