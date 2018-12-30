/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import kp.cbs.utils.RNG;

/**
 *
 * @author Marc
 */
public final class BattleResult
{
    private final int money;
    private final int elo;
    
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
        float levelElo = (int) (self.getLevelAverage() / enemy.getLevelAverage() * 4f);
        float classElo = (int) ((self.getTeamClass().ordinal() - enemy.getTeamClass().ordinal() + 10f) / 10f * 4f);
        float aliveElo = self.getAliveCount() / (float) self.size() * 4f;
        
        return (int) (levelElo + classElo + aliveElo);
    }
    
    public final int getMoney() { return money; }
    
    public final int getElo() { return elo; }
}
