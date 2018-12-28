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
public enum CreatureClass
{
    E("E"),
    E2("E+"),
    E3("E++"),
    D("D"),
    D2("D+"),
    D3("D++"),
    C("C"),
    C2("C+"),
    C3("C++"),
    B("B"),
    B2("B+"),
    B3("B++"),
    A("A"),
    A2("A+"),
    A3("A++"),
    S("S"),
    S2("S+"),
    S3("S++"),
    SS("SS"),
    SS2("SS+"),
    SS3("SS++"),
    SSS("SSS");
    
    private final String name;
    
    private CreatureClass(String name)
    {
        this.name = name;
    }
    
    public final String getName() { return name; }
    
    @Override
    public final String toString() { return name; }
}
