/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.effects.HealDamageEffect;
import kp.cbs.creature.elements.ElementalTypeSerializer;
import kp.cbs.creature.race.Evolution;
import kp.cbs.creature.race.Race;
import kp.udl.UDLManager;
import kp.udl.autowired.Autowired;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;
import kp.udl.exception.UDLException;
import kp.udl.input.ElementPool;

/**
 *
 * @author Asus
 */
public final class Serializer
{
    private Serializer() {}
    
    private static final UDLManager UDL = new UDLManager();
    static
    {
        UDL.enableDefaultUseCommand();
        
        Serializer.registerSerializer(new ColorSerializer());
        Serializer.registerSerializer(new ElementalTypeSerializer());
        Serializer.registerSerializer(Race.SERIALIZER);
        Serializer.registerSerializer(Attack.SERIALIZER);
        Serializer.registerSerializer(AttackModel.SERIALIZER);
        Serializer.registerSerializer(HealDamageEffect.SERIALIZER);
        Serializer.registerSerializer(Evolution.EVO_COND_SERIALIZER);
    }
    
    /* Read ops */
    public static final UDLValue read(Reader reader) throws UDLException, IOException { return UDL.read(reader); }
    public static final UDLValue read(InputStream is) throws UDLException, IOException { return UDL.read(is); }
    public static final UDLValue read(Path file) throws UDLException, IOException { return UDL.read(file); }
    public static final UDLValue read(String path, String... more) throws UDLException, IOException { return UDL.read(Paths.get(path, more)); }
    public static final UDLValue read(String path) throws UDLException, IOException { return UDL.read(Paths.get(path)); }
    public static final UDLValue decode(String text) throws UDLException, IOException { return UDL.decode(text); }
    
    /* Write ops */
    public static final void write(UDLValue value, Writer writer) throws UDLException, IOException { UDL.write(writer, value); }
    public static final void write(UDLValue value, OutputStream os) throws UDLException, IOException { UDL.write(os, value); }
    public static final void write(UDLValue value, Path file) throws UDLException, IOException { UDL.write(file, value); }
    public static final void write(UDLValue value, String path) throws UDLException, IOException { UDL.write(Paths.get(path), value); }
    public static final void write(UDLValue value, String path, String... more) throws UDLException, IOException { UDL.write(Paths.get(path, more), value); }
    public static final String encode(UDLValue value, boolean wrapped) throws UDLException, IOException { return UDL.encode(value, wrapped); }
    
    /* Autowired ops */
    public static final UDLValue extract(Object obj) { return UDL.extract(obj); }
    public static final <T> T inject(UDLValue value, Class<T> jclass) { return UDL.inject(jclass, value); }
    
    public static final UDLValue rawExtract(Object obj) { return Autowired.extract(obj, UDL.getSerializerManager(), true); }
    public static final <T> T rawInject(UDLValue value, Class<T> jclass) { return Autowired.inject(jclass, value, UDL.getSerializerManager(), true); }
    
    
    /* Other ops */
    public static final void registerSerializer(AutowiredSerializer<?> serializer)
    {
        UDL.getSerializerManager().registerSerializer(serializer);
    }
    
    public static final void registerUsable(String name, ElementPool pool)
    {
        UDL.registerUsable(name, pool);
    }
}
