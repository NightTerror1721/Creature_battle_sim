/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.altered;

import kp.cbs.battle.BattleUpdater;

/**
 *
 * @author Asus
 */
public abstract class AlteredState implements BattleUpdater
{
    public abstract AlteredStateId getId();
    
    public abstract boolean isEnabled();
}
