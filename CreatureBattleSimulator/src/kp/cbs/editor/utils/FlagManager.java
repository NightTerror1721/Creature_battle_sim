/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.util.HashMap;

/**
 *
 * @author Marc
 */
public final class FlagManager<K>
{
    private final HashMap<K, Boolean> flags = new HashMap<>();
    
    public final boolean isEnabled(K flagId)
    {
        return flags.getOrDefault(flagId, Boolean.FALSE);
    }
    
    public final boolean isDisabled(K flagId)
    {
        return !flags.getOrDefault(flagId, Boolean.FALSE);
    }
    
    public final void enable(K flagId)
    {
        flags.put(flagId, Boolean.TRUE);
    }
    
    public final void disable(K flagId)
    {
        flags.put(flagId, Boolean.FALSE);
    }
    
    public final void disableAll()
    {
        flags.clear();
    }
}
