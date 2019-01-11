/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import kp.udl.data.UDLValue;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class Config
{
    private Config() {}
    
    private static final Map<String, UDLValue> CONFIG;
    
    static
    {
        Map<String, UDLValue> map;
        try
        {
            var path = Paths.concat(Paths.DATA, "config.udl");
            var base = Serializer.read(path);
            map = base.getMap().entrySet().stream().collect(Collectors.toMap(
                    e -> e.getKey().getString(),
                    e -> e.getValue()));
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            map = new HashMap<>();
        }
        CONFIG = map;
    }
    
    private static <T> T get(String key, T defVal, Function<UDLValue, T> mapper)
    {
        var val = CONFIG.getOrDefault(key, null);
        return val == null ? defVal : mapper.apply(val);
    }
    
    private static <T> Function<UDLValue, T[]> valueToArray(final Function<UDLValue, T> mapper, final IntFunction<T[]> arrayGenerator)
    {
        return value -> value.getList().stream().map(mapper).toArray(arrayGenerator);
    }
    
    private static float[] valueToFloatArray(UDLValue value)
    {
        var list = value.getList();
        var array = new float[list.size()];
        var inc = 0;
        for(var val : list)
            array[inc++] = val.getFloat();
        return array;
    }
    
    private static <T> Function<UDLValue, List<T>> valueToList(final Function<UDLValue, T> mapper)
    {
        return value -> value.getList().stream().map(mapper).collect(Collectors.toList());
    }
    
    private static <K, V> Function<UDLValue, Map<K, V>> valueToMap(final Function<UDLValue, K> keyMapper, final Function<UDLValue, V> valueMapper)
    {
        return value -> value.getList().stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }
    
    public static final UDLValue get(String key, UDLValue defaultValue) { return CONFIG.getOrDefault(key, defaultValue); }
    public static final UDLValue get(String key) { return CONFIG.getOrDefault(key, UDLValue.NULL); }
    
    public static final <O> O get(String key, O defaultValue, Class<O> jclass) { return get(key, defaultValue, val -> Serializer.inject(val, jclass)); }
    public static final <O> O get(String key, Class<O> jclass) { return get(key, null, jclass); }
    
    public static final int getInt(String key, int defaultValue) { return get(key, defaultValue, UDLValue::getInt); }
    public static final int getInt(String key) { return getInt(key, 0); }
    
    public static final long getLong(String key, long defaultValue) { return get(key, defaultValue, UDLValue::getLong); }
    public static final long getLong(String key) { return getLong(key, 0L); }
    
    public static final float getFloat(String key, float defaultValue) { return get(key, defaultValue, UDLValue::getFloat); }
    public static final float getFloat(String key) { return getFloat(key, 0f); }
    
    public static final double getDouble(String key, double defaultValue) { return get(key, defaultValue, UDLValue::getDouble); }
    public static final double getDouble(String key) { return getDouble(key, 0d); }
    
    public static final String getString(String key, String defaultValue) { return get(key, defaultValue, UDLValue::getString); }
    public static final String getString(String key) { return getString(key, ""); }
    
    public static final UDLValue[] getArray(String key, UDLValue[] defaultValue)
    {
        return get(key, defaultValue, valueToArray(Function.identity(), UDLValue[]::new));
    }
    public static final UDLValue[] getArray(String key) { return getArray(key, new UDLValue[] {}); }
    
    public static final <T> T[] getArray(String key, T[] defaultValue, IntFunction<T[]> arrayGenerator, Class<T> jclass)
    {
        return get(key, defaultValue, valueToArray(v -> Serializer.inject(v, jclass), arrayGenerator));
    }
    public static final <T> T[] getArray(String key, IntFunction<T[]> arrayGenerator, Class<T> jclass) { return getArray(key, null, arrayGenerator, jclass); }
    
    public static final int[] getIntArray(String key, int[] defaultValue)
    {
        return get(key, defaultValue, val -> val.getList().stream().mapToInt(UDLValue::getInt).toArray());
    }
    public static final int[] getIntArray(String key) { return getIntArray(key, new int[] {}); }
    
    public static final long[] getLongArray(String key, long[] defaultValue)
    {
        return get(key, defaultValue, val -> val.getList().stream().mapToLong(UDLValue::getLong).toArray());
    }
    public static final long[] getLongArray(String key) { return getLongArray(key, new long[] {}); }
    
    public static final float[] getFloatArray(String key, float[] defaultValue)
    {
        return get(key, defaultValue, Config::valueToFloatArray);
    }
    public static final float[] getFloatArray(String key) { return getFloatArray(key, new float[] {}); }
    
    public static final double[] getDoubleArray(String key, double[] defaultValue)
    {
        return get(key, defaultValue, val -> val.getList().stream().mapToDouble(UDLValue::getDouble).toArray());
    }
    public static final double[] getDoubleArray(String key) { return getDoubleArray(key, new double[] {}); }
    
    public static final String[] getStringArray(String key, String[] defaultValue)
    {
        return get(key, defaultValue, valueToArray(UDLValue::getString, String[]::new));
    }
    public static final String[] getStringArray(String key) { return getStringArray(key, new String[] {}); }
    
    public static final List<UDLValue> getList(String key, List<UDLValue> defaultValue)
    {
        return get(key, defaultValue, UDLValue::getList);
    }
    public static final List<UDLValue> getList(String key) { return getList(key, Collections.emptyList()); }
    
    public static final <O> List<O> getList(String key, List<O> defaultValue, Class<O> jclass)
    {
        return get(key, defaultValue, valueToList(v -> Serializer.inject(v, jclass)));
    }
    public static final <O> List<O> getList(String key, Class<O> jclass) { return getList(key, Collections.emptyList(), jclass); }
    
    public static final List<Integer> getIntList(String key, List<Integer> defaultValue)
    {
        return get(key, defaultValue, valueToList(UDLValue::getInt));
    }
    public static final List<Integer> getIntList(String key) { return getIntList(key, Collections.emptyList()); }
    
    public static final List<Long> getLongList(String key, List<Long> defaultValue)
    {
        return get(key, defaultValue, valueToList(UDLValue::getLong));
    }
    public static final List<Long> getLongList(String key) { return getLongList(key, Collections.emptyList()); }
    
    public static final List<Float> getFloatList(String key, List<Float> defaultValue)
    {
        return get(key, defaultValue, valueToList(UDLValue::getFloat));
    }
    public static final List<Float> getFloatList(String key) { return getFloatList(key, Collections.emptyList()); }
    
    public static final List<Double> getDoubleList(String key, List<Double> defaultValue)
    {
        return get(key, defaultValue, valueToList(UDLValue::getDouble));
    }
    public static final List<Double> getDoubleList(String key) { return getDoubleList(key, Collections.emptyList()); }
    
    public static final List<String> getStringList(String key, List<String> defaultValue)
    {
        return get(key, defaultValue, valueToList(UDLValue::getString));
    }
    public static final List<String> getStringList(String key) { return getStringList(key, Collections.emptyList()); }
}
