/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import kp.cbs.creature.Creature;
import kp.cbs.utils.RNG;

/**
 *
 * @author Marc
 */
public final class BattleResult
{
    private final boolean selfWinner;
    private final int money;
    private final int elo;
    private final Creature cached;
    
    public BattleResult(Team self, Team enemy, RNG rng)
    {
        if(self.hasAnyAlive())
        {
            this.money = computeMoney(enemy, rng);
            this.elo = Math.max(0, computeElo(self, enemy));
        }
        else
        {
            this.money = -computeMoney(self, rng);
            this.elo = Math.min(0, -computeElo(enemy, self));
        }
        this.selfWinner = self.hasAnyAlive() && !enemy.hasAnyAlive();
        this.cached = null;
    }
    public BattleResult(Creature cached)
    {
        this.money = 0;
        this.elo = 0;
        this.selfWinner = true;
        this.cached = Objects.requireNonNull(cached);
    }
    
    private static int computeMoney(Team team, RNG rng)
    {
        int money = 0;
        int len = team.size();
        for(int i=0;i<len;i++)
        {
            var base = team.getCreature(i).getCreatureClass().ordinal() + 1;
            money += 200 * base / 15 * base;
        }
        money /= len;
        var dif = Math.max(10, money / 20);
        dif = rng.d(dif * 2) - dif;
        return money + dif;
    }
    
    private static int computeElo(Team self, Team enemy)
    {
        float levelElo = (float) enemy.getLevelAverage() / self.getLevelAverage();
        float classElo = (enemy.getTeamClass().ordinal() - self.getTeamClass().ordinal() + 10f) / 10f;
        float aliveElo = self.getAliveCount() / (float) self.size();
        
        return (int) (levelElo * classElo * aliveElo * 10f);
    }
    
    public final int getMoney() { return money; }
    
    public final int getElo() { return elo; }
    
    public final boolean isSelfWinner() { return selfWinner; }
    
    public final boolean hasCached() { return cached != null; }
    public final Creature getCached() { return cached; }
}
