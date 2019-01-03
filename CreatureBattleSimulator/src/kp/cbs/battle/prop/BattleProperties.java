/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.prop;

import java.util.LinkedList;
import java.util.Objects;
import kp.cbs.battle.Team.SearchFirstBehabior;
import kp.cbs.battle.Team.SearchNextBehabior;
import kp.cbs.utils.Utils;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class BattleProperties
{
    @Property private int minTeamLen = 1;
    @Property private int maxTeamLen = 1;
    
    @Property private String music;
    
    @Property private int intelligence;
    
    @Property private SearchFirstBehabior firstBehabior = SearchFirstBehabior.FIRST;
    @Property private SearchNextBehabior nextBehabior = SearchNextBehabior.SEARCH;
    
    @Property private LinkedList<CreatureEntry> entries = new LinkedList<>();
    @Property private LinkedList<CreatureProperties> required = new LinkedList<>();
    
    public final void setMinTeamLength(int length) { this.minTeamLen = Math.max(1, length); }
    public final void setMaxTeamLength(int length) { this.maxTeamLen = Math.max(1, length); }
    
    public final int getMinTeamLength() { return minTeamLen; }
    public final int getMaxTeamLength() { return maxTeamLen; }
    
    public final void setMusic(String music) { this.music = music; }
    public final String getMusic() { return music; }
    
    public final void setIntelligence(int intel) { this.intelligence = Utils.range(0, 255, intel); }
    public final int getIntelligence() { return intelligence; }
    
    public final void setSearchFirstBehabior(SearchFirstBehabior behabior) { this.firstBehabior = Objects.requireNonNullElse(behabior, SearchFirstBehabior.FIRST); }
    public final SearchFirstBehabior getSearchFirstBehabior() { return firstBehabior; }
    
    public final void setSearchNextBehabior(SearchNextBehabior behabior) { this.nextBehabior = Objects.requireNonNullElse(behabior, SearchNextBehabior.SEARCH); }
    public final SearchNextBehabior getSearchNextBehabior() { return nextBehabior; }
    
    
    /*public final Encounter createEncounter()
    {
        var encounter = new Encounter();
        var rng = new RNG();
        
        encounter.setMusic(music);
        encounter.setIntelligence(AIIntelligence.create(intelligence));
        
        if(firstBehabior != null)
            encounter.setSearchFirstBehabior(firstBehabior);
        if(nextBehabior != null)
            encounter.setSearchNextBehabior(nextBehabior);
        
        var minTeam = Math.min(Math.min(1, minTeamLen), Math.min(1, maxTeamLen));
        var maxTeam = Math.max(Math.min(1, minTeamLen), Math.min(1, maxTeamLen));
        var teamSize = minTeam == maxTeam ? maxTeam : rng.d(maxTeam - minTeam + 1) + minTeam;
        
        
    }*/
    
    
    
    
    
    
    public static final class CreatureEntry
    {
        @Property(set = "setMinElo")        private int minElo = 0;
        @Property(set = "setProbability")   private int probability = 1;
        @Property                           private boolean unique;
        @Property                           private CreatureProperties creature;
        
        public final void setMinElo(int elo) { this.minElo = Math.max(0, elo); }
        public final int getMinElo() { return minElo; }
        
        public final void setProbability(int prob) { this.probability = Math.max(1, prob); }
        public final int getProbability() { return probability; }
        
        public final void setUnique(boolean flag) { this.unique = flag; }
        public final boolean isUnique() { return unique; }
        
        public final void setCreatureProperties(CreatureProperties creature) { this.creature = creature; }
        public final CreatureProperties getCreatureProperties() { return creature; }
    }
}
