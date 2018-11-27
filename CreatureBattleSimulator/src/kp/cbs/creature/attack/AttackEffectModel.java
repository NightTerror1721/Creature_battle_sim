/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

/**
 *
 * @author Asus
 */
public class AttackEffectModel
{
    private int id;
    private String name;
    private String description;
    
    
    public static final class FlagModel
    {
        private int id;
        private String name;
        
    }
    
    public static enum FlagType
    {
        DEC_PERCENT,
        BYTE_PERCENT,
        POWER,
        TARGET,
        ELEMENT,
        STAT,
        STATUS;
    }
}
