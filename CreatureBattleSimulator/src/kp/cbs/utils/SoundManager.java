/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 *
 * @author mpasc
 */
public final class SoundManager
{
    private SoundManager() {}
    
    private static final HashMap<String, SoundNode> SOUNDS = new HashMap<>();
    
    public static final void loadSounds(boolean autoClose)
    {
        TinySound.init();
        TinySound.setGlobalVolume(0.4);
        if(autoClose)
            Runtime.getRuntime().addShutdownHook(new Thread(TinySound::shutdown));
        try
        {
            Files.list(Paths.SOUND).filter(f -> f.getFileName().toString().endsWith(".wav"))
                    .forEach(f -> {
                Sound sound = TinySound.loadSound(f.toFile());
                String name = f.getFileName().toString();
                SOUNDS.put(name.substring(0, name.length() - 4), new SoundNode(sound));
            });
        }
        catch(IOException ex) {}
    }
    
    public static final Sound getSound(String name)
    {
        SoundNode node = SOUNDS.get(name);
        return node == null ? null : node.sound;
    }
    
    public static final void playSound(String name)
    {
        SoundNode node = SOUNDS.get(name);
        node.sound.play();
        /*if(node != null)
            node.play = true;*/
    }
    
    /*public static final void update()
    {
        for(SoundNode node : SOUNDS.values())
        {
            if(node.play)
            {
                node.play = false;
                node.sound.play();
            }
        }
    }*/
    
    public static final Music loadMusic(MusicModel model)
    {
        var file = Paths.concat(Paths.MUSIC, model.getName() + ".wav");
        var music = TinySound.loadMusic(file.toFile(), true);
        music.setLoopPositionByFrame((int) model.getLoopPosition());
        music.setLoop(true);
        return music;
    }
    public static final Music loadMusic(String name)
    {
        var model = MusicModel.getModel(name);
        return model == null ? null : loadMusic(model);
    }
    
    public static final String[] getAvaliableMusics()
    {
        var base = Paths.MUSIC;
        if(!Files.isDirectory(base))
            return new String[] {};
        try
        {
            return Files.list(base)
                    .map(f -> f.getFileName().toString())
                    .filter(f -> f.toLowerCase().endsWith(".wav"))
                    .sorted(String::compareTo)
                    .toArray(String[]::new);
        }
        catch(IOException ex) { return new String[] {}; }
    }
    
    private static final class SoundNode
    {
        private final Sound sound;
        //private boolean play;
        
        private SoundNode(Sound sound)
        {
            this.sound = sound;
            //play = false;
        }
    }
}
