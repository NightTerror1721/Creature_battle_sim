/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.awt.Color;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class ColorSerializer extends AutowiredSerializer<Color>
{
    public ColorSerializer() { super(Color.class); }

    @Override
    public final UDLValue serialize(Color value)
    {
        return UDLValue.valueOf(new int[] { value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha() });
    }

    @Override
    public final Color unserialize(UDLValue value)
    {
        int len = value.size();
        int r = len < 1 ? 0 : Utils.range(0, 255, value.getInt(0));
        int g = len < 2 ? 0 : Utils.range(0, 255, value.getInt(1));
        int b = len < 3 ? 0 : Utils.range(0, 255, value.getInt(2));
        if(len < 4)
            return new Color(r, g, b);
        int a = Utils.range(0, 255, value.getInt(3));
        return new Color(r, g, b, a);
    }
    
}
