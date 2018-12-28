/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.awt.Color;

/**
 *
 * @author Asus
 */
public final class LifebarUtils
{
    private LifebarUtils() {}
    
    
    public static final Color computeColor(int maxHp)
    {
        int r, g, b;
        
        switch(hpToLevel(maxHp))
        {
            case 0: // HP < 2500
                r = value(0, maxHp, 200, -200);
                g = value(0, maxHp, 225, -25);
                b = 0;
                break;
            case 1: // HP < 5000
                r = 0;
                g = value(1, maxHp, 200, -75);
                b = 0;
                break;
            case 2: // HP < 7500
                r = 0;
                g = value(2, maxHp, 125, 75);
                b = value(2, maxHp, 0, 250);
                break;
            case 3: // HP < 10000
                r = value(3, maxHp, 0, 50);
                g = value(3, maxHp, 200, -90);
                b = value(3, maxHp, 250, -110);
                break;
            case 4: // HP < 12500
                r = value(4, maxHp, 50, 75);
                g = value(4, maxHp, 110, -110);
                b = value(4, maxHp, 140, 10);
                break;
            case 5: // HP < 15000
                r = value(5, maxHp, 125, 25);
                g = 0;
                b = value(5, maxHp, 150, -100);
                break;
            default: // HP >= 15000
                r = 150;
                g = 0;
                b = 50;
                break;
        }
        
        return new Color(r, g, b);
    }
    
    private static int hpToLevel(int hp) { return Math.max(0, hp / 2500); }
    
    private static int value(int level, int value, int base, int addition)
    {
        value = (value - (level * 2500)) / 25;
        float ratio = (value - (level * 2500)) / 2500f;
        return Utils.range(0, 255, (int) (base + (ratio * addition)));
    }
}
