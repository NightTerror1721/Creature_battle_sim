/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import kp.udl.autowired.Property;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class MusicModel
{
    @Property
    private String name;
    
    @Property
    private long loopPosition;
    
    /*@Property
    private long endLoopPosition;*/
    
    public final String getName() { return name; }
    
    public final long getLoopPosition() { return loopPosition; }
    
    //public final long getEndLoopPosition() { return endLoopPosition; }
    
    
    private static final HashMap<String, MusicModel> MODELS = new HashMap<>();
    static
    {
        var file = Paths.concat(Paths.MUSIC, "config.udl");
        try
            {
            if(Files.isReadable(file))
            {
                var base = Serializer.read(file);
                var list = base.getList("music");
                for(var theme : list)
                {
                    var model = Serializer.inject(theme, MusicModel.class);
                    MODELS.put(model.name, model);
                }
            }
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
    public static final MusicModel getModel(String name)
    {
        return MODELS.getOrDefault(name, null);
    }
    
    public static final String[] getAllModelNames(boolean sorted)
    {
        return sorted
                ?MODELS.values().stream()
                        .map(MusicModel::getName)
                        .sorted(String::compareTo)
                        .toArray(String[]::new)
                : MODELS.values().stream()
                        .map(MusicModel::getName)
                        .toArray(String[]::new);
    }
}
