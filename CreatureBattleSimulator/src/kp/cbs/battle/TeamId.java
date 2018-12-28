/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

/**
 *
 * @author Asus
 */
public enum TeamId
{
    SELF, ENEMY;
    public final TeamId invert() { return this == SELF ? ENEMY : SELF; }
}
