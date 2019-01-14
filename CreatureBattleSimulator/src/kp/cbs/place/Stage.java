/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.place;

import java.awt.Window;
import kp.cbs.PlayerGame;
import kp.cbs.creature.Creature;

/**
 *
 * @author Asus
 */
public interface Stage
{
    boolean isLeage();
    
    int getWins();
    int getAccumulatedElo();
    int getAccumulatedMoney();
    
    int getRemainingBattles();
    
    void forceLose();
    
    void startNextBattle(Window parent, PlayerGame game, Creature... selfCreatures);
}
