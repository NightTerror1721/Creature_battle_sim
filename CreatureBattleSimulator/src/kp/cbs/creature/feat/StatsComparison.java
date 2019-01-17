/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.feat;

import kp.cbs.creature.Creature;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public final class StatsComparison
{
    private final int[] adds = new int[StatId.count()];
    private final int[] news = new int[StatId.count()];
    
    public StatsComparison(Creature creature, int level)
    {
        level = Utils.range(2, 100, level);
        var nature = creature.getNature();
        var oldLevel = level - 1;
        for(var i = 0; i < adds.length; i++)
        {
            var stat = creature.getStat(StatId.decode(i));
            news[i] = stat.getPureValue(level, nature);
            adds[i] = news[i] - stat.getPureValue(oldLevel, nature);
        }
    }
    
    public final int getStatAddition(StatId stat) { return adds[stat.ordinal()]; }
    public final int getNewStatValue(StatId stat) { return news[stat.ordinal()]; }
    public final int getStatValue(StatId stat, boolean addition) { return (addition ? adds : news)[stat.ordinal()]; }
}
