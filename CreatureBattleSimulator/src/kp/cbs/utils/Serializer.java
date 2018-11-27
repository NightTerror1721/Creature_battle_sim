/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import kp.cbs.creature.elements.ElementalTypeSerializer;
import kp.cbs.creature.race.RaceSerializer;
import kp.udl.UDLManager;
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
        
        Serializer.registerSerializer(new ElementalTypeSerializer());
        Serializer.registerSerializer(new RaceSerializer());
    }
    
    /* Read ops */
    public static final UDLValue read(Reader reader) throws UDLException, IOException { return UDL.read(reader); }
    public static final UDLValue read(InputStream is) throws UDLException, IOException { return UDL.read(is); }
    public static final UDLValue read(File file) throws UDLException, IOException { return UDL.read(file); }
    public static final UDLValue decode(String text) throws UDLException, IOException { return UDL.decode(text); }
    
    /* Write ops */
    public static final void write(Writer writer, UDLValue value) throws UDLException, IOException { UDL.write(writer, value); }
    public static final void write(OutputStream os, UDLValue value) throws UDLException, IOException { UDL.write(os, value); }
    public static final void write(File file, UDLValue value) throws UDLException, IOException { UDL.write(file, value); }
    public static final String encode(UDLValue value, boolean wrapped) throws UDLException, IOException { return UDL.encode(value, wrapped); }
    
    /* Autowired ops */
    public static final UDLValue extract(Object obj) { return UDL.extract(obj); }
    public static final <T> T inject(Class<T> jclass, UDLValue value) { return UDL.inject(jclass, value); }
    
    
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
