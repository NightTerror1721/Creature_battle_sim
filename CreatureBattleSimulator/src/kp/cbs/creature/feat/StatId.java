/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

/**
 *
 * @author Asus
 */
public enum StatId
{
    HEALTH_POINTS("PS"),
    
    ATTACK("Ataque"),
    DEFENSE("Defensa"),
    
    SPECIAL_ATTACK("Ataque Especial"),
    SPECIAL_DEFENSE("Defensa Especial"),
    
    SPEED("Velocidad");
    
    
    private String name;
    
    private StatId(String name)
    {
        this.name = name;
    }
    
    public final String getName() { return name; }
    
    @Override
    public final String toString() { return name; }
    
    public static final StatId decode(int index)
    {
        switch(index)
        {
            default:
            case 0: return HEALTH_POINTS;
            case 1: return ATTACK;
            case 2: return DEFENSE;
            case 3: return SPECIAL_ATTACK;
            case 4: return SPECIAL_DEFENSE;
            case 5: return SPEED;
        }
    }
}
