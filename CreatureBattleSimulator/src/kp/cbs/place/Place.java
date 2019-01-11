/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.place;

import java.awt.Window;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.cbs.PlayerGame;
import kp.cbs.battle.Battle;
import kp.cbs.battle.Encounter;
import kp.cbs.battle.prop.BattleProperties;
import kp.cbs.battle.prop.BattlePropertiesPool;
import kp.cbs.creature.Creature;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.autowired.Property;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class Place
{
    private String name;
    
    @Property private String wildBattle = "";
    @Property private String trainerBattle = "";
    @Property private LinkedList<Challenge> challenges = new LinkedList<>();
    @Property private LinkedHashMap<String, String[]> travels;
    
    private BattleProperties wildCache;
    private BattleProperties trainerCache;
    
    public final String getName() { return name; }
    
    public final int getChallengeCount() { return challenges.size(); }
    public final List<Challenge> getAllChallenges() { return Collections.unmodifiableList(challenges); }
    
    public final boolean hasTrainerBattle() { return trainerBattle != null && !trainerBattle.isBlank(); }
    public final boolean hasWildBattle() { return wildBattle != null && !wildBattle.isBlank(); }
    
    public final Stream<String> streamAvailableTravels(PlayerGame game)
    {
        return travels.entrySet().stream()
                .filter(e -> game.isIdsPassed(e.getValue()))
                .map(Map.Entry::getKey);
    }
    public final String[] getAvailableTravels(PlayerGame game)
    {
        return streamAvailableTravels(game).sorted().toArray(String[]::new);
    }
    public final int getAvailableTravelsCount(PlayerGame game)
    {
        return (int) streamAvailableTravels(game).count();
    }
    
    private Encounter generateWildEncounter()
    {
        if(wildCache == null)
        {
            wildCache = BattlePropertiesPool.load(wildBattle);
            if(wildCache == null)
                throw new IllegalStateException("Battle " + wildBattle + " not found");
        }
        return wildCache.createEncounter();
    }
    
    private Encounter generateTrainerEncounter()
    {
        if(trainerCache == null)
        {
            trainerCache = BattlePropertiesPool.load(trainerBattle);
            if(trainerCache == null)
                throw new IllegalStateException("Battle " + trainerBattle + " not found");
        }
        return trainerCache.createEncounter();
    }
    
    public final Creature startWildBattle(Window parent, PlayerGame game, Creature... selfCreatures)
    {
        if(!hasWildBattle())
            return null;
        
        if(selfCreatures == null || selfCreatures.length < 1)
                throw new IllegalStateException();
        
        var encounter = generateWildEncounter();
        encounter.setExperienceBonus(1f);
        encounter.setWildBattle(true);
        for(var creature : selfCreatures)
            encounter.getSelfTeam().addCreature(creature);

        var result = Battle.initiate(parent, game, encounter);
        if(!result.hasCached())
            return null;
        return result.getCached();
    }
    
    public final void startTrainerBattle(Window parent, PlayerGame game, Creature... selfCreatures)
    {
        if(!hasTrainerBattle())
            return;
        
        if(selfCreatures == null || selfCreatures.length < 1)
                throw new IllegalStateException();
        
        var encounter = generateTrainerEncounter();
        encounter.setExperienceBonus(1.25f);
        for(var creature : selfCreatures)
            encounter.getSelfTeam().addCreature(creature);

        Battle.initiate(parent, game, encounter);
    }
    
    
    
    
    
    public static final void save(Place place)
    {
        var path = Paths.concat(Paths.PLACES, place.name + ".place");
        try
        {
            var base = Serializer.extract(place);
            Serializer.write(base, path);
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
    public static final Place load(String name)
    {
        if(name == null || name.isBlank())
            return null;
        var path = Paths.concat(Paths.PLACES, name + ".place");
        try
        {
            if(!Files.isReadable(path))
                return null;
            var base = Serializer.read(path);
            var game = Serializer.inject(base, Place.class);
            game.name = name;
            return game;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
    
    public static final String[] getAllSavedNames()
    {
        try
        {
            return Files.list(Paths.PLACES)
                    .map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(".place"))
                    .map(n -> n.substring(0, n.length() - 6))
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
