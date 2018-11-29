/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.Creature;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public final class FighterTurnState
{
    public final Creature self, enemy;
    public final BattleCommandManager bcm;
    public final RNG rng;
    private int turn;
    private boolean turnEnd;
    private boolean canAttack;
    private WeatherId weather;
    
    public FighterTurnState(Creature self, Creature enemy, BattleCommandManager bcm, RNG rng, int turn, boolean turnEnd, WeatherId weather)
    {
        this.self = Objects.requireNonNull(self);
        this.enemy = Objects.requireNonNull(enemy);
        this.bcm = Objects.requireNonNull(bcm);
        this.rng = Objects.requireNonNull(rng);
        this.turn = turn;
        this.turnEnd = turnEnd;
        this.canAttack = true;
        this.weather = weather;
    }
    
    public final int getTurn() { return turn; }
    public final boolean isTurnEnd() { return turnEnd; }
    public final boolean canAttack() { return canAttack; }
    public final WeatherId getWeather() { return weather; }
    public final void setWeather(WeatherId weather) { this.weather = weather; }
    
    public final void dissableAttack() { this.canAttack = false; }
    
    final void forward()
    {
        if(turnEnd)
            turn++;
        turnEnd = !turnEnd;
    }
}
