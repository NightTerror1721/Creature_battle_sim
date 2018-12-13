/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Asus
 */
public final class Paths
{
    private Paths() {}
    
    public static final Path MUSIC = initDir("data", "audio", "music");
    public static final Path SOUND = initDir("data", "audio", "sound");
    public static final Path ATTACKS = initDir("data", "attacks");
    public static final Path RACES = initDir("data", "races");
    
    
    public static final Path get(String path, String... more) { return java.nio.file.Paths.get(path, more); }
    public static final Path get(String path) { return java.nio.file.Paths.get(path); }
    
    public static final Path concat(Path base, Path other) { return base.resolve(other); }
    public static final Path concat(Path base, String path) { return base.resolve(path); }
    public static final Path concat(Path base, String path, String... more) { return base.resolve(base.getFileSystem().getPath(path, more)); }
    
    private static Path initDir(String path, String... more)
    {
        try
        {
            var p = get(path, more);
            if(!Files.isDirectory(p))
                Files.createDirectories(p);
            return p;
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
            return null;
        }
    }
}
