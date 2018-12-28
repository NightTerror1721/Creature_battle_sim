/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.battle.weather.Weather;
import kp.cbs.creature.Creature;
import kp.cbs.utils.Formula;
import kp.cbs.utils.RNG;

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
    
    private int turn;
    
    private Creature selfCreature;
    private Creature enemyCreature;
    
    private Weather weather;
    
    public BattleCore(Team self, Team enemy)
    {
        this.selfTeam = Objects.requireNonNull(self);
        this.enemyTeam = Objects.requireNonNull(enemy);
    }
    
    public final void setBattle(Battle battle) { this.battle = battle; }
    
    public final int getTurn() { return turn; }
    public final void increaseTurn() { turn++; }
    
    public final Creature getFighter(TeamId team)
    {
        return team == TeamId.SELF ? selfCreature : enemyCreature;
    }
    
    public final RNG getCoreRng() { return coreRng; }
    
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
        return new FighterTurnState(cself, cenemy, bcm, rng, turn, false, wid);
    }
    
    public final void goFirstCreature(TeamId team)
    {
        battle.clearText();
        if(team == TeamId.SELF)
        {
            selfCreature = selfTeam.findFirst(selfRng, Team.SearchFirstBehabior.FIRST);
            battle.insertMessage("¡" + selfCreature.getName() + " toma la iniciativa para luchar!");
        }
        else
        {
            enemyCreature = enemyTeam.findFirst(enemyRng);
            battle.insertMessage("¡El enemigo " + enemyCreature.getName() + " se prepara para luchar!");
        }
        battle.updateCreatureInterface(team);
        battle.sleep(2500);
    }
    
    public final void goNextCreature(TeamId team)
    {
        battle.clearText();
        if(team == TeamId.SELF)
        {
            selfCreature =  TeamViewer.open(battle, selfTeam, selfCreature, true);
            battle.insertMessage("¡Sacas a luchar a " + selfCreature.getName() + "!");
        }
        else 
        {
            var wid = weather == null ? null : weather.getId();
            enemyCreature = enemyTeam.findNext(selfCreature, enemyRng, wid);
            battle.insertMessage("¡El enemigo saca a luchar a " + enemyCreature.getName() + "!");
        }
        
        battle.updateCreatureInterface(team);
        battle.sleep(2500);
    }
    
    public final void start()
    {
        battle.clearText();
        battle.updateCreatureInterface(TeamId.SELF);
        battle.updateCreatureInterface(TeamId.ENEMY);
        
        battle.insertMessage("¡Te han retado para luchar!");
        battle.sleep(2000);
        
        battle.insertMessage("Dispones de " + selfTeam.size() + " criatura" + (selfTeam.size() > 1 ? "s." : "."));
        battle.sleep(500);
        
        battle.insertMessage("El enemigo tiene " + enemyTeam.size() + " criatura" + (enemyTeam.size() > 1 ? "s." : "."));
        battle.sleep(2000);
        
        goFirstCreature(TeamId.ENEMY);
        goFirstCreature(TeamId.SELF);
    }
}
