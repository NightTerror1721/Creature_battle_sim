/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import javax.swing.JOptionPane;
import kp.cbs.battle.Battle.ButtonsMenuState;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.battle.weather.ElectricStorm;
import kp.cbs.battle.weather.Fog;
import kp.cbs.battle.weather.Hail;
import kp.cbs.battle.weather.IntenseSun;
import kp.cbs.battle.weather.Rain;
import kp.cbs.battle.weather.Sandstorm;
import kp.cbs.battle.weather.Weather;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.Creature;
import kp.cbs.creature.altered.AlteredStateId;
import kp.cbs.creature.altered.Burn;
import kp.cbs.creature.altered.Confusion;
import kp.cbs.creature.altered.Curse;
import kp.cbs.creature.altered.Freezing;
import kp.cbs.creature.altered.Intoxication;
import kp.cbs.creature.altered.Nightmare;
import kp.cbs.creature.altered.Paralysis;
import kp.cbs.creature.altered.Poisoning;
import kp.cbs.creature.altered.Sleep;
import kp.cbs.creature.altered.Sleepiness;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.creature.feat.NormalStat;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.state.StateId;
import kp.cbs.utils.Formula;
import kp.cbs.utils.RNG;
import kp.cbs.utils.SoundManager;

/**
 *
 * @author Asus
 */
public final class BattleCore
{
    private Battle battle;
    
    private final Team selfTeam;
    private final Team enemyTeam;
    
    private final RNG selfRng = new RNG();
    private final RNG enemyRng = new RNG();
    private final RNG coreRng = new RNG();
    
    private final BattleCommandManager bcm = new BattleCommandManager();
    
    private Creature selfCreature;
    private Creature enemyCreature;
    
    private AttackTurn selfTurn;
    private AttackTurn enemyTurn;
    
    private Creature selfChange;
    private Creature enemyChange;
    
    private Weather weather;
    
    public BattleCore(Team self, Team enemy)
    {
        this.selfTeam = Objects.requireNonNull(self);
        this.enemyTeam = Objects.requireNonNull(enemy);
    }
    
    public final void setBattle(Battle battle) { this.battle = battle; }
    
    public final Creature getFighter(TeamId team)
    {
        return team == TeamId.SELF ? selfCreature : enemyCreature;
    }
    
    public final RNG getCoreRng() { return coreRng; }
    
    public final WeatherId getWeatherId() { return weather == null ? null : weather.getId(); }
    
    public final AIIntelligence getEnemyIntelligence() { return enemyTeam.getIntelligence(); }
    
    public final BattleResult createResult() { return new BattleResult(selfTeam, enemyTeam, coreRng); }
    
    public final boolean isCurrentAlive(TeamId team)
    {
        var creature = getFighter(team);
        return creature.isAlive();
    }
    
    public final int getExperieneGained(float bonus)
    {
        var base = enemyCreature.getFeaturesManager().computeExperienceBase();
        return Formula.experienceGained(selfCreature.getLevel(), enemyCreature.getLevel(), base, bonus);
    }
    
    public final FighterTurnState generateFighterState(TeamId team)
    {
        var cself = getFighter(team);
        var cenemy = getFighter(team.invert());
        var rng = team == TeamId.SELF ? selfRng : enemyRng;
        var wid = weather == null ? null : weather.getId();
        return new FighterTurnState(cself, cenemy, bcm, rng, false, wid);
    }
    
    public final void goFirstCreature(TeamId team)
    {
        battle.clearText();
        if(team == TeamId.SELF)
        {
            selfCreature = selfTeam.findFirst(selfRng, Team.SearchFirstBehabior.FIRST);
            battle.insertMessage("¡" + selfCreature.getName() + " toma la iniciativa para luchar!");
            selfCreature.setFighterId(team);
        }
        else
        {
            enemyCreature = enemyTeam.findFirst(enemyRng);
            battle.insertMessage("¡El enemigo " + enemyCreature.getName() + " se prepara para luchar!");
            enemyCreature.setFighterId(team);
        }
        removeAttackInProgress(team);
        
        battle.updateCreatureInterface(team);
        battle.sleep(2000);
    }
    
    public final void goNextCreature(TeamId team, boolean canPreventChange)
    {
        battle.clearText();
        if(team == TeamId.SELF)
        {
            selfCreature =  TeamViewer.open(battle, selfTeam, selfCreature, true);
            battle.insertMessage("¡Sacas a luchar a " + selfCreature.getName() + "!");
            selfCreature.setFighterId(team);
        }
        else 
        {
            var wid = weather == null ? null : weather.getId();
            enemyCreature = enemyTeam.findNext(selfCreature, enemyRng, wid);
            
            if(canPreventChange && selfTeam.size() > 1 &&
                    JOptionPane.showConfirmDialog(battle, "El enemigo va a sacar a luchar a un luchador de raza " +
                    enemyCreature.getRace().getName() + ". ¿Quieres cambiar de luchador?",
                    "Cambio de Luchador", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                var creature = TeamViewer.open(battle, selfTeam, selfCreature, false);
                if(creature != null && creature != selfCreature)
                {
                    selfCreature = creature;
                    battle.insertMessage("¡Sacas a luchar a " + selfCreature.getName() + "!");
                    selfCreature.setFighterId(team);
                    removeAttackInProgress(TeamId.SELF);
                    battle.updateCreatureInterface(TeamId.SELF);
                    battle.sleep(1500);
                }
            }
            
            battle.insertMessage("¡El enemigo saca a luchar a " + enemyCreature.getName() + "!");
            enemyCreature.setFighterId(team);
        }
        removeAttackInProgress(team);
        
        battle.updateCreatureInterface(team);
        battle.sleep(2000);
    }
    
    public final void start()
    {
        battle.setButtonsState(ButtonsMenuState.DISABLED);
        
        battle.clearText();
        battle.updateCreatureInterface(TeamId.SELF);
        battle.updateCreatureInterface(TeamId.ENEMY);
        
        battle.insertMessage("¡Te han retado para luchar!");
        battle.sleep(1000);
        
        battle.insertMessage("Dispones de " + selfTeam.size() + " criatura" + (selfTeam.size() > 1 ? "s." : "."));
        battle.sleep(500);
        
        battle.insertMessage("El enemigo tiene " + enemyTeam.size() + " criatura" + (enemyTeam.size() > 1 ? "s." : "."));
        battle.sleep(1000);
        
        goFirstCreature(TeamId.ENEMY);
        goFirstCreature(TeamId.SELF);
        
        battle.selectActionState();
    }
    
    public final boolean applyChange(TeamId team)
    {
        if(team == TeamId.SELF)
        {
            if(selfChange == null)
                return false;
            if(selfChange == selfCreature)
                return false;
            applyChange(team, selfChange);
            selfChange = null;
        }
        else
        {
            if(enemyChange == null)
                return false;
            if(enemyChange == enemyCreature)
                return false;
            applyChange(team, enemyChange);
            enemyChange = null;
        }
        return true;
    }
    
    private void applyChange(TeamId team, Creature newc)
    {
        if(team == TeamId.SELF)
        {
            var oldc = selfCreature;
            selfCreature = null;
            
            battle.clearText();
            battle.insertMessage("Has retirado a " + oldc.getName() + "...");
            battle.updateCreatureInterface(team);
            battle.sleep(2000);
            
            selfCreature = newc;
            battle.clearText();
            battle.insertMessage("¡Sacas a luchar a " + newc.getName() + "!");
            battle.sleep(1000);
            battle.updateCreatureInterface(team);
            battle.sleep(2000);
        }
        else
        {
            var oldc = enemyCreature;
            enemyCreature = null;
            
            battle.clearText();
            battle.insertMessage("El enemigo retira a " + oldc.getName() + ".");
            battle.updateCreatureInterface(team);
            battle.sleep(2000);
            
            enemyCreature = newc;
            battle.clearText();
            battle.insertMessage("¡El enemigo saca a luchar a " + newc.getName() + "!");
            battle.sleep(1000);
            battle.updateCreatureInterface(team);
            battle.sleep(2000);
        }
    }
    
    public final boolean prepareChange(TeamId team)
    {
        if(team == TeamId.SELF)
        {
            selfChange = TeamViewer.open(battle, selfTeam, selfCreature, false);
            if(selfChange == null || selfChange == selfCreature)
            {
                selfChange = null;
                return false;
            }
        }
        else
        {
            if(enemyTeam.getAliveCount() < 2)
                return false;

            var wid = weather == null ? null : weather.getId();
            enemyChange = enemyTeam.findNext(selfCreature, enemyRng, wid);
            if(enemyChange == null || enemyChange == enemyCreature)
            {
                enemyChange = null;
                return false;
            }
        }
        return true;
    }
    
    public final Creature openTeam()
    {
        return TeamViewer.open(battle, selfTeam, selfCreature, false);
    }
    
    public final void applyBattleCommands(BattleCommandManager bcm)
    {
        while(bcm.hasMoreCommands())
        {
            var cmd = bcm.pollCommand();
            switch(cmd.getType())
            {
                case MESSAGE: battle.insertMessage(cmd.get(0)); break;
                case PLAY_SOUND: SoundManager.playSound(cmd.get(0)); break;
                case WAIT: battle.sleep(((Number)cmd.get(0)).intValue()); break;
                case DAMAGE: {
                    TeamId target = cmd.get(0);
                    var creature = getFighter(target);
                    int points = cmd.get(1);
                    
                    creature.getHealthPoints().damage(points);
                    var goal = creature.getCurrentHealthPoints();
                    
                    battle.modifBarlifePlayer(target, goal, false);
                } break;
                case HEAL: {
                    TeamId target = cmd.get(0);
                    var creature = getFighter(target);
                    int points = cmd.get(1);
                    
                    creature.getHealthPoints().heal(points);
                    var goal = creature.getCurrentHealthPoints();
                    
                    battle.modifBarlifePlayer(target, goal, true);
                } break;
                case STAT_MODIFICATION: {
                    TeamId target = cmd.get(0);
                    var creature = getFighter(target);
                    StatId stat = cmd.get(1);
                    if(stat == StatId.HEALTH_POINTS)
                        break;
                    int levels = cmd.get(2);
                    
                    ((NormalStat) creature.getStat(stat)).modifyStat(levels);
                } break;
                case STATE_MODIFICATION: {
                    TeamId target = cmd.get(0);
                    var creature = getFighter(target);
                    StateId state = cmd.get(1);
                    
                    switch(state)
                    {
                        case ACCURACY: creature.getAccuracy().modifyStat(cmd.get(2)); break;
                        case EVASION: creature.getEvasion().modifyStat(cmd.get(2)); break;
                        case CRITICAL_HIT: creature.setCriticalHitBonus(cmd.get(2)); break;
                        case INTIMIDATED: creature.setIntimidated(cmd.get(2)); break;
                        case RESTING: creature.setResting(cmd.get(2)); break;
                    }
                } break;
                case ALTERATION: {
                    var bcm2 = new BattleCommandManager();
                    TeamId target = cmd.get(0);
                    var creature = getFighter(target);
                    AlteredStateId id = cmd.get(1);
                    var rng = target == TeamId.SELF ? selfRng : enemyRng;
                    
                    switch(id)
                    {
                        case BURN: creature.addAlteration(rng, bcm2, new Burn()); break;
                        case CONFUSION: creature.addAlteration(rng, bcm2, new Confusion()); break;
                        case CURSE: creature.addAlteration(rng, bcm2, new Curse()); break;
                        case FREEZING: creature.addAlteration(rng, bcm2, new Freezing()); break;
                        case INTOXICATION: creature.addAlteration(rng, bcm2, new Intoxication()); break;
                        case NIGHTMARE: creature.addAlteration(rng, bcm2, new Nightmare()); break;
                        case PARALYSIS: creature.addAlteration(rng, bcm2, new Paralysis()); break;
                        case POISONING: creature.addAlteration(rng, bcm2, new Poisoning()); break;
                        case SLEEP: creature.addAlteration(rng, bcm2, new Sleep(cmd.get(2))); break;
                        case SLEEPINESS: creature.addAlteration(rng, bcm2, new Sleepiness()); break;
                    }
                    
                    applyBattleCommands(bcm2);
                } break;
                case WEATHER_CHANGE: {
                    var bcm2 = new BattleCommandManager();
                    WeatherId id = cmd.get(0);
                    int turns = cmd.get(1);
                    
                    applyNewWeather(id, turns, bcm2);
                    
                    applyBattleCommands(bcm2);
                } break;
            }
        }
    }
    
    private void applyNewWeather(WeatherId id, int turns, BattleCommandManager bcm)
    {
        if(weather == null || weather.getId() != id)
        {
            stopWeather(bcm);
            switch(id)
            {
                case RAIN: weather = new Rain(turns); break;
                case INTENSE_SUN: weather = new IntenseSun(turns); break;
                case SANDSTORM: weather = new Sandstorm(turns); break;
                case HAIL: weather = new Hail(turns); break;
                case FOG: weather = new Fog(turns); break;
                case ELECTRIC_STORM: weather = new ElectricStorm(turns); break;
            }
            
            if(weather != null)
                weather.start(bcm);
        }
    }
    
    private void stopWeather(BattleCommandManager bcm)
    {
        if(weather == null)
            return;
        
        weather.end(bcm);
        weather = null;
    }
    
    public final void updateWeather()
    {
        if(weather == null)
            return;
        
        battle.clearText();
        
        weather.battleUpdate(bcm, coreRng, selfCreature, enemyCreature);
        
        if(weather.isEnd())
            stopWeather(bcm);
        
        applyBattleCommands(bcm);
    }
    
    public final void updateAlterations(FighterTurnState state)
    {
        battle.clearText();
        
        state.self.getAlterationManager().battleUpdate(state);
        applyBattleCommands(state.bcm);
    }
    
    public final void applyAttack(FighterTurnState state, Attack attack)
    {
        battle.insertMessage(state.self.getName() + " usa " + attack.getName() + "...");
        battle.sleep(1000);
        
        if(!checkAttackPrecision(state, attack))
        {
            battle.insertMessage("¡El ataque de " + state.self.getName() + " falló!");
            battle.sleep(1000);
            return;
        }
        
        var team = state.self.getFighterId();
        if(team == TeamId.SELF)
            selfTurn = attack.getTurn(0);
        else enemyTurn = attack.getTurn(0);
        
        attack.usePP(1);
        
        applyNextTurn(state);
    }
    
    public final void applyNextTurn(FighterTurnState state)
    {
        var team = state.self.getFighterId();
        var turn = team == TeamId.SELF ? selfTurn : enemyTurn;
        
        if(turn == null)
            return;
        
        turn.apply(state);
        applyBattleCommands(state.bcm);
        
        battle.sleep(1000);
        
        if(team == TeamId.SELF)
            selfTurn = turn.getNextTurn();
        else enemyTurn = turn.getNextTurn();
    }
    
    public final boolean hasAttackInProgress(FighterTurnState state)
    {
        var team = state.self.getFighterId();
        return (team == TeamId.SELF ? selfTurn : enemyTurn) != null;
    }
    
    public final void removeAttackInProgress(TeamId team)
    {
        if(team == TeamId.SELF)
            selfTurn = null;
        else enemyTurn = null;
    }
    
    
    private boolean checkAttackPrecision(FighterTurnState state, Attack attack)
    {
        if(attack.isInfallible())
            return true;
        float ratio = Formula.computePrecision(state.self, state.enemy, weather == null ? null : weather.getId());
        int pre = (int) (attack.getPrecision() * ratio);
        return state.rng.d100(pre);
    }
    
    public final boolean isGameOver()
    {
        return !selfTeam.hasAnyAlive() || !enemyTeam.hasAnyAlive();
    }
    
    public final void checkChanges()
    {
        var canPreventChange = true;
        if(!isCurrentAlive(TeamId.SELF))
        {
            goNextCreature(TeamId.SELF, false);
            canPreventChange = false;
        }
        if(!isCurrentAlive(TeamId.ENEMY))
            goNextCreature(TeamId.ENEMY, canPreventChange);
    }
    
    public final boolean isSelfWinner() { return isGameOver() && selfTeam.hasAnyAlive(); }
}
