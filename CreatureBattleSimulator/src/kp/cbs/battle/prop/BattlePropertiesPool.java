/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.prop;

import java.io.IOException;
import java.nio.file.Files;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class BattlePropertiesPool
{
    private BattlePropertiesPool() {}
    
    public static final BattlePropertiesPool load(String name)
    {
        var path = Paths.concat(Paths.BATTLES, name + ".bprops");
        if(!Files.isReadable(path))
            return null;
        try
        {
            var base = Serializer.read(path);
            return Serializer.inject(base, BattlePropertiesPool.class); 
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
    
    public static final boolean store(String name, BattlePropertiesPool props)
    {
        var path = Paths.concat(Paths.BATTLES, name + ".bprops");
        try
        {
            var base = Serializer.extract(props);
            Serializer.write(base, path);
            return true;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
    }
}
