/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.util.Random;

/**
 *
 * @author Asus
 */
public final class RNG
{
    private final Random rand;
    private long seed;
    
    public RNG(long seed)
    {
        rand = new Random(this.seed = seed);
    }
    public RNG() { this(new Random().nextLong()); }
    
    public final void setSeed(long seed)
    {
        this.seed = seed;
        rand.setSeed(seed);
    }
    public final long getSeed() { return seed; }
    
    public final int getInt() { return rand.nextInt(); }
    public final int getInt(int limit) { return rand.nextInt(limit); }
    public final int getInt(int min, int max) { return rand.nextInt(max - min) + min; }
    
    public final long getLong() { return rand.nextLong(); }
    public final long getLong(long limit) { return rand.nextLong() % limit; }
    public final long getLong(long min, long max) { return (rand.nextLong() % (max - min)) + min; }
    
    public final float getFloat() { return rand.nextFloat(); }
    public final float getFloat(float max) { return rand.nextFloat() * max; }
    public final float getFloat(float min, float max) { return rand.nextFloat() * (max - min) + min; }
    
    public final double getDouble() { return rand.nextDouble(); }
    public final double getDouble(double max) { return rand.nextDouble() * max; }
    public final double getDouble(double min, double max) { return rand.nextDouble() * (max - min) + min; }
    
    public final int d2() { return rand.nextInt(2); }
    public final int d3() { return rand.nextInt(3); }
    public final int d4() { return rand.nextInt(4); }
    public final int d5() { return rand.nextInt(5); }
    public final int d6() { return rand.nextInt(6); }
    public final int d7() { return rand.nextInt(7); }
    public final int d8() { return rand.nextInt(8); }
    public final int d9() { return rand.nextInt(9); }
    public final int d10() { return rand.nextInt(10); }
    public final int d12() { return rand.nextInt(12); }
    public final int d16() { return rand.nextInt(16); }
    public final int d20() { return rand.nextInt(20); }
    public final int d24() { return rand.nextInt(24); }
    public final int d32() { return rand.nextInt(32); }
    public final int d50() { return rand.nextInt(50); }
    public final int d64() { return rand.nextInt(64); }
    public final int d100() { return rand.nextInt(100); }
    public final int d128() { return rand.nextInt(128); }
    public final int d200() { return rand.nextInt(200); }
    public final int d256() { return rand.nextInt(256); }
    public final int d500() { return rand.nextInt(500); }
    public final int d512() { return rand.nextInt(512); }
    public final int d1000() { return rand.nextInt(1000); }
    public final int d1024() { return rand.nextInt(1024); }
    public final int d65536() { return rand.nextInt(65536); }
    public final int d(int sides) { return rand.nextInt(sides); }
    
    public final boolean d2(int prob) { return d2() < prob; }
    public final boolean d3(int prob) { return d3() < prob; }
    public final boolean d4(int prob) { return d4() < prob; }
    public final boolean d5(int prob) { return d5() < prob; }
    public final boolean d6(int prob) { return d6() < prob; }
    public final boolean d7(int prob) { return d7() < prob; }
    public final boolean d8(int prob) { return d8() < prob; }
    public final boolean d9(int prob) { return d9() < prob; }
    public final boolean d10(int prob) { return d10() < prob; }
    public final boolean d12(int prob) { return d12() < prob; }
    public final boolean d16(int prob) { return d16() < prob; }
    public final boolean d20(int prob) { return d20() < prob; }
    public final boolean d24(int prob) { return d24() < prob; }
    public final boolean d32(int prob) { return d32() < prob; }
    public final boolean d50(int prob) { return d50() < prob; }
    public final boolean d64(int prob) { return d64() < prob; }
    public final boolean d100(int prob) { return d100() < prob; }
    public final boolean d128(int prob) { return d128() < prob; }
    public final boolean d200(int prob) { return d200() < prob; }
    public final boolean d256(int prob) { return d256() < prob; }
    public final boolean d500(int prob) { return d500() < prob; }
    public final boolean d512(int prob) { return d512() < prob; }
    public final boolean d1000(int prob) { return d1000() < prob; }
    public final boolean d1024(int prob) { return d1024() < prob; }
    public final boolean d65536(int prob) { return d65536() < prob; }
    public final boolean d(int sides, int prob) { return d(sides) < prob; }
    
    public final boolean d2(float prob) { return d2() < prob * 2; }
    public final boolean d3(float prob) { return d3() < prob * 3; }
    public final boolean d4(float prob) { return d4() < prob * 4; }
    public final boolean d5(float prob) { return d5() < prob * 5; }
    public final boolean d6(float prob) { return d6() < prob * 6; }
    public final boolean d7(float prob) { return d7() < prob * 7; }
    public final boolean d8(float prob) { return d8() < prob * 8; }
    public final boolean d9(float prob) { return d9() < prob * 9; }
    public final boolean d10(float prob) { return d10() < prob * 10; }
    public final boolean d12(float prob) { return d12() < prob * 12; }
    public final boolean d16(float prob) { return d16() < prob * 16; }
    public final boolean d20(float prob) { return d20() < prob * 20; }
    public final boolean d24(float prob) { return d24() < prob * 24; }
    public final boolean d32(float prob) { return d32() < prob * 32; }
    public final boolean d50(float prob) { return d50() < prob * 50; }
    public final boolean d64(float prob) { return d64() < prob * 64; }
    public final boolean d100(float prob) { return d100() < prob * 100; }
    public final boolean d128(float prob) { return d128() < prob * 128; }
    public final boolean d200(float prob) { return d200() < prob * 200; }
    public final boolean d256(float prob) { return d256() < prob * 256; }
    public final boolean d500(float prob) { return d500() < prob * 500; }
    public final boolean d512(float prob) { return d512() < prob * 512; }
    public final boolean d1000(float prob) { return d1000() < prob * 1000; }
    public final boolean d1024(float prob) { return d1024() < prob * 1024; }
    public final boolean d65536(float prob) { return d65536() < prob * 65536; }
    public final boolean d(int sides, float prob) { return d(sides) < prob * sides; }
}
