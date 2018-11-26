/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

/**
 *
 * @author Asus
 */
public enum Growth
{
    NORMAL("Normal"),
    QUICK("Rápido"),
    SLOW("Lento"),
    PARABOLIC("Parabólico"),
    ERRATIC("Errático"),
    FLUCTUATING("Fluctuante");
    
    private final String name;
    
    private Growth(String name)
    {
        this.name = name;
    }
    
    public final String getName() { return name; }
}
