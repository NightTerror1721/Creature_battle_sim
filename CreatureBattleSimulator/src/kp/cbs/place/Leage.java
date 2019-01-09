/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.place;

import java.awt.Window;
import java.io.IOException;
import java.nio.file.Files;
import kp.cbs.PlayerGame;
import kp.cbs.battle.Battle;
import kp.cbs.battle.BattleResult;
import kp.cbs.battle.Encounter;
import kp.cbs.battle.prop.BattleProperties;
import kp.cbs.battle.prop.BattlePropertiesPool;
import kp.cbs.creature.Creature;
import kp.cbs.utils.GlobalId;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.autowired.Property;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public class Leage extends GlobalId
{
    private String name = "UNKNOWN_LEAGE";
    
    @Property private int battlesPerStage = 1;
    @Property private String finalBattle = "";
    @Property private LeageStageProperties[] stages = {};
    
    private BattleProperties finalBattleCache;
    
    public final String getName() { return name; }
    public final int getBattlesPerStage() { return battlesPerStage; }
    
    public final boolean isFinalBattleEnabled(int currentElo)
    {
        return stages.length < 1 || stages[stages.length - 1].maxElo <= currentElo;
    }
    
    public final LeageStage createStage(int currentElo)
    {
        if(stages.length < 1)
            return null;
        
        for(var i = stages.length - 1; i >= 0; i--)
            if(stages[i].minElo <= currentElo)
                return new LeageStage(stages[i], currentElo);
        return new LeageStage(stages[0], stages[0].minElo);
    }
    
    private Encounter generateFinalEncounter()
    {
        if(finalBattleCache == null)
        {
            finalBattleCache = BattlePropertiesPool.load(finalBattle);
            if(finalBattleCache == null)
                throw new IllegalStateException("Battle " + finalBattle + " not found");
        }
        return finalBattleCache.createEncounter();
    }
    
    public final BattleResult startFinalBattle(Window parent, PlayerGame game, Creature... selfCreatures)
    {
        if(selfCreatures == null || selfCreatures.length < 1)
                throw new IllegalStateException();
        
        var encounter = generateFinalEncounter();
        encounter.setExperienceBonus(1.75f);
        for(var creature : selfCreatures)
            encounter.getSelfTeam().addCreature(creature);

        return Battle.initiate(parent, game, encounter);
    }
    
    
    public static final class LeageStageProperties
    {
        @Property private int minElo = 0;
        @Property private int maxElo = 0;
        @Property private String battle = "";
        
        private BattleProperties battleCache;
        
        private BattleProperties props()
        {
            if(battleCache == null)
            {
                battleCache = BattlePropertiesPool.load(battle);
                if(battleCache == null)
                    throw new IllegalStateException("Battle " + battle + " not found");
            }
            return battleCache;
        }
    }
    
    public final class LeageStage
    {
        private final LeageStageProperties stage;
        private final int currentElo;
        private int remainingBattles;
        private int wins;
        private int accumulatedElo;
        
        private LeageStage(LeageStageProperties stage, int currentElo)
        {
            this.stage = stage;
            this.currentElo = currentElo;
            this.remainingBattles = battlesPerStage;
            this.wins = 0;
        }
        
        public final int getWins() { return wins; }
        public final int getRemainingBattles() { return remainingBattles; }
        public final boolean hasMoreBattles() { return remainingBattles > 0; }
        
        public final int getAccumulatedElo() { return accumulatedElo; }
        
        public final BattleResult startNextBattle(Window parent, PlayerGame game, Creature... selfCreatures)
        {
            if(selfCreatures == null || selfCreatures.length < 1)
                throw new IllegalStateException();
            
            var encounter = stage.props().createEncounter(currentElo, stage.minElo, stage.maxElo);
            encounter.setExperienceBonus(1.5f);
            for(var creature : selfCreatures)
                encounter.getSelfTeam().addCreature(creature);
            
            var result = Battle.initiate(parent, game, encounter);
            accumulatedElo += result.getElo();
            remainingBattles--;
            if(result.isSelfWinner())
                wins++;
            
            return result;
        }
    }
    
    
    
    
    
    public static final void store(Leage leage)
    {
        var path = Paths.concat(Paths.LEAGES, leage.name + ".leage");
        try
        {
            var base = Serializer.extract(leage);
            Serializer.write(base, path);
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
    public static final Leage load(String name)
    {
        var path = Paths.concat(Paths.LEAGES, name + ".leage");
        try
        {
            if(!Files.isReadable(path))
                return new Leage();
            var base = Serializer.read(path);
            var leage = Serializer.inject(base, Leage.class);
            leage.name = name;
            return leage;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return new Leage();
        }
    }
    
    public static final String[] getAllLeageNames()
    {
        try
        {
            return Files.list(Paths.LEAGES)
                    .map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(".leage"))
                    .map(n -> n.substring(0, n.length() - 6))
                    .sorted(String::compareTo)
                    .toArray(String[]::new);
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return new String[] {};
        }
    }
}
