/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.state;

/**
 *
 * @author Asus
 */
public enum StateId
{
    ACCURACY("Precisión"),
    EVASION("Evasión"),
    CRITICAL_HIT("Golpe Crítico"),
    INTIMIDATED("Amedrentado"),
    RESTING("Descansando");
    
    public final String name;
    
    private StateId(String name) { this.name = name; }
    
    public final String getName() { return name; }
}
