/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.cmd;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.Creature;
import kp.cbs.creature.altered.AlteredStateId;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.state.StateId;

/**
 *
 * @author Asus
 */
public final class BattleCommandManager implements Iterable<BattleCommand>
{
    private final LinkedList<BattleCommand> cmds = new LinkedList<>();
    
    private BattleCommandManager addCommand(BattleCommandType type, Object... data) { cmds.addLast(new BattleCommand(type, data)); return this; }
    
    public final boolean hasMoreCommands() { return !cmds.isEmpty(); }
    
    public final BattleCommand pollCommand() { return cmds.removeFirst(); }

    @Override
    public final Iterator<BattleCommand> iterator() { return cmds.iterator(); }
    
    
    public final BattleCommandManager message(String message)
    {
        return addCommand(BattleCommandType.MESSAGE, Objects.requireNonNull(message));
    }
    
    
    public final BattleCommandManager damage(Creature target, int points)
    {
        return addCommand(BattleCommandType.DAMAGE, target.getFighterId(), Math.abs(points));
    }
    
    
    public final BattleCommandManager heal(Creature target, int points)
    {
        return addCommand(BattleCommandType.HEAL, target.getFighterId(), Math.abs(points));
    }
    
    
    public final BattleCommandManager statModification(Creature target, StatId stat, int levels)
    {
        if(stat != StatId.HEALTH_POINTS)
            return addCommand(BattleCommandType.STAT_MODIFICATION, target.getFighterId(), levels);
        return this;
    }
    
    
    private BattleCommandManager stateModification(Creature target, StateId state, Object extra)
    {
        return addCommand(BattleCommandType.STATE_MODIFICATION, target.getFighterId(), state, extra);
    }
    public final BattleCommandManager accuracyModification(Creature target, int levels)
    {
        return stateModification(target, StateId.ACCURACY, levels);
    }
    public final BattleCommandManager evasionModification(Creature target, int levels)
    {
        return stateModification(target, StateId.EVASION, levels);
    }
    public final BattleCommandManager criticalHitBonus(Creature target, boolean flag)
    {
        return stateModification(target, StateId.CRITICAL_HIT, flag);
    }
    public final BattleCommandManager resting(Creature target, boolean flag)
    {
        return stateModification(target, StateId.RESTING, flag);
    }
    public final BattleCommandManager intimidated(Creature target, boolean flag)
    {
        return stateModification(target, StateId.INTIMIDATED, flag);
    }
    
    
    public final BattleCommandManager alteration(Creature target, AlteredStateId astate, Object... extra)
    {
        Object[] pars = new Object[extra.length + 2];
        pars[0] = target.getFighterId();
        pars[1] = astate;
        System.arraycopy(extra, 0, pars, 1, extra.length);
        return addCommand(BattleCommandType.ALTERATION, pars);
    }
    
    
    public final BattleCommandManager weatherChange(WeatherId weather, int turns)
    {
        return addCommand(BattleCommandType.WEATHER_CHANGE, weather, turns);
    }
    
    
    public final BattleCommandManager waitTime(long millis)
    {
        return addCommand(BattleCommandType.WAIT, millis);
    }
}
