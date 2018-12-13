/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.race;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class RacePool
{
    private RacePool() {}
    
    private static final HashMap<Integer, Race> RACES = new HashMap<>();
    private static List<Race> ALL;
    
    public static final Race getRace(int id)
    {
        Race race = RACES.getOrDefault(id, null);
        return race == null ? loadRace(id) : race;
    }
    
    public static final List<Race> getAllRaces()
    {
        if(ALL != null)
            return ALL;;
        if(!Files.isDirectory(Paths.RACES))
            return List.of();
        try
        {
            return ALL = Files.list(Paths.RACES)
                    .filter(RacePool::isValidFile)
                    .map(RacePool::mapFileToRace)
                    .collect(Collectors.toUnmodifiableList());
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return List.of();
        }
    }
    
    private static Race loadRace(int id)
    {
        Path raceFile = Paths.concat(Paths.RACES, generateFilename(id));
        try
        {
            var race = readFileRace(raceFile);
            race.setId(id);
            RACES.put(id, race);
            return race;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return new Race();
        }
    }
    
    private static Race readFileRace(Path raceFile) throws IOException, UDLException
    {
        if(!Files.isReadable(raceFile))
            throw new IOException("File not found or not redeable");
        var base = Serializer.read(raceFile);
        var race = Serializer.rawInject(base, Race.class);
        if(race == null)
            throw new IOException("Fail to read Race in file: " + raceFile);
        return race;
    }
    private static Race mapFileToRace(Path raceFile)
    {
        try
        {
            var race = readFileRace(raceFile);
            RACES.put(race.getId(), race);
            return race;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return new Race();
        }
    }
    
    private static final Pattern MODEL_FILE_PATTERN = Pattern.compile("[0-9]+\\.race");
    
    private static boolean isValidFile(Path file)
    {
        return MODEL_FILE_PATTERN.matcher(file.getFileName().toString()).matches();
    }
    
    private static final String generateFilename(int id) { return id + ".race"; }
}
