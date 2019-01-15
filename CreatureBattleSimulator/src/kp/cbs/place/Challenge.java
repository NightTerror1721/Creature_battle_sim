/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.place;

import java.awt.Window;
import java.util.LinkedList;
import kp.cbs.PlayerGame;
import kp.cbs.battle.Battle;
import kp.cbs.battle.Encounter;
import kp.cbs.battle.prop.BattlePropertiesPool;
import kp.cbs.creature.Creature;
import kp.cbs.utils.GlobalId;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class Challenge extends GlobalId
{
    @Property private String name = "";
    @Property private String description = "";
    @Property private boolean unique;
    @Property private String[] battles = {};
    
    public final String getName() { return name; }
    public final String getDescription() { return description; }
    
    public final boolean isEnabled(PlayerGame game)
    {
        return !unique || !isCompleted(game);
    }
    
    public final boolean isCompleted(PlayerGame game) { return game.isIdPassed(getId()); }
    
    public final boolean isUnique() { return unique; }
    
    private Encounter generateEncounter(int index)
    {
        var cache = BattlePropertiesPool.load(battles[index]);
        if(cache == null)
            return null;
        return cache.createEncounter();
    }
    
    public final ChallengeStage createStage() { return new ChallengeStage(); }
    
    @Override
    public final String toString() { return getName(); }
    
    
    public final class ChallengeStage implements Stage
    {
        private final LinkedList<Encounter> encounters;
        private final int all;
        private int wins;
        private int accumulatedMoney;
        
        private ChallengeStage()
        {
            this.encounters = new LinkedList<>();
            for(var i = 0; i < battles.length; i++)
            {
                var enc = generateEncounter(i);
                if(enc != null)
                    encounters.add(enc);
            }
            this.all = encounters.size();
        }
        
        @Override
        public final boolean isLeage() { return false; }

        @Override
        public final int getWins() { return wins; }
        
        public final boolean allWinned() { return wins > 0 && wins >= all; }

        @Override
        public final int getAccumulatedElo() { return 0; }

        @Override
        public final int getAccumulatedMoney() { return accumulatedMoney; }

        @Override
        public final void forceLose()
        {
            encounters.clear();
            wins = 0;
            accumulatedMoney = Math.min(0, accumulatedMoney);
        }
        
        @Override
        public final int getRemainingBattles() { return encounters.size(); }
        
        @Override
        public final void startNextBattle(Window parent, PlayerGame game, Creature... selfCreatures)
        {
            if(selfCreatures == null || selfCreatures.length < 1 || encounters.isEmpty())
                    throw new IllegalStateException();

            var encounter = encounters.removeFirst();
            encounter.setExperienceBonus(1.5f);
            for(var creature : selfCreatures)
                encounter.getSelfTeam().addCreature(creature);

            var result = Battle.initiate(parent, game, encounter);
            accumulatedMoney += result.getMoney();
            if(result.isSelfWinner())
                wins++;
        }
    }
}
