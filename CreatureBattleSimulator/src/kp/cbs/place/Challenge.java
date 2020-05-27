/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.place;

import java.awt.Window;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;
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
    @Property private String[] required = {};
    
    public final void setName(String name) { this.name = name == null ? "" : name; }
    public final void setDescription(String description) { this.description = description == null ? "" : description; }
    
    public final String getName() { return name; }
    public final String getDescription() { return description; }
    
    public final boolean isEnabled(PlayerGame game)
    {
        return !unique || !isCompleted(game);
    }
    
    public final void setUnique(boolean flag) { this.unique = flag; }
    
    public final boolean isBloqued(PlayerGame game) { return required.length > 0 && !game.isIdsPassed(required); }
    
    public final boolean isCompleted(PlayerGame game) { return game.isIdPassed(getId()); }
    
    public final boolean isUnique() { return unique; }
    
    public final void setBattles(String[] battles)
    {
        this.battles = battles == null
                ? new String[] {}
                : Arrays.copyOf(battles, battles.length);
    }
    
    public final void setRequired(String[] required)
    {
        this.required = required == null
                ? new String[] {}
                : Arrays.copyOf(required, required.length);
    }
    
    private Encounter generateEncounter(int index)
    {
        var cache = BattlePropertiesPool.load(battles[index]);
        if(cache == null)
            return null;
        return cache.createEncounter();
    }
    
    public final ChallengeStage createStage() { return new ChallengeStage(); }
    
    public final String[] getAllBattles() { return Arrays.copyOf(battles, battles.length); }
    
    public final Stream<String> streamBattles()
    {
        return Stream.of(battles);
    }
    
    public final String[] getAllRequired() { return Arrays.copyOf(required, required.length); }
    
    public final Stream<String> streamRequired()
    {
        return Stream.of(required);
    }
    
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

            var result = Battle.initiate(parent, game, encounter, true, false);
            accumulatedMoney += result.getMoney();
            if(result.isSelfWinner())
                wins++;
        }
    }
}
