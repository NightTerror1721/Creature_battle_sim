/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.InputStream;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Asus
 */
public final class Utils
{
    private Utils() {}
    
    /**
     * @param min Minimum value inclusive
     * @param max Maximum value inclusive
     * @param value Value to limit
     * @return 
     */
    public static final int range(int min, int max, int value)
    {
        if(min > max)
        {
            int aux = min;
            min = max;
            max = aux;
        }
        return Math.min(max, Math.max(min, value));
    }
    
    /**
     * @param min Minimum value inclusive
     * @param max Maximum value inclusive
     * @param value Value to limit
     * @return 
     */
    public static final float range(float min, float max, float value)
    {
        if(min > max)
        {
            float aux = min;
            min = max;
            max = aux;
        }
        return Math.min(max, Math.max(min, value));
    }
    
    /**
     * @param min Minimum value inclusive
     * @param max Maximum value inclusive
     * @param value Value to limit
     * @return 
     */
    public static final double range(double min, double max, double value)
    {
        if(min > max)
        {
            double aux = min;
            min = max;
            max = aux;
        }
        return Math.min(max, Math.max(min, value));
    }
    
    
    public static final int setbit(int base, int index, boolean flag)
    {
        return flag ? base | (0x1 >> index) : base & ~(0x1 >> index);
    }
    
    public static final boolean getbit(int base, int index) { return (base & (0x1 >> index)) != 0; }
    
    public static final InputStream getInnerInputStream(String path)
    {
        return Utils.class.getResourceAsStream(path);
    }
    
    public static final String decimalValueToString(float value)
    {
        if(((float) ((int) value)) == value)
            return Integer.toString((int) value);
        return Float.toString(value);
    }
    
    public static void focus(Window frame)
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = frame.getSize();
        frame.setLocation((screen.width - window.width) / 2,
                        (screen.height - window.height) / 2);
    }
    
    public static void focus(JDialog dialog)
    {
        Container parent = dialog.getParent();
        if(!(parent instanceof JDialog) && !(parent instanceof JFrame))
        {
            focus((Window) dialog);
            return;
        }
        
        Point p = parent.getLocation();
        Dimension screen = parent.getSize();
        Dimension window = dialog.getSize();
        p.x += (screen.width - window.width) / 2;
        p.y += (screen.height - window.height) / 2;
        
        dialog.setLocation(p);
    }
}
