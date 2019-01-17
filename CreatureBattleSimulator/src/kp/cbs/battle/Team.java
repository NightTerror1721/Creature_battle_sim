/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.ArrayList;
import kp.cbs.battle.cmd.BattleCommandManager;
import kp.cbs.battle.weather.WeatherId;
import kp.cbs.creature.Creature;
import kp.cbs.creature.CreatureClass;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.creature.attack.effects.AIScore;
import kp.cbs.utils.Formula;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public final class Team
{
    private final ArrayList<Creature> creatures = new ArrayList<>(6);
    private SearchFirstBehabior searchFirst = SearchFirstBehabior.FIRST;
    private SearchNextBehabior searchNext = SearchNextBehabior.ORDERED;
    private AIIntelligence intel = AIIntelligence.createDummy();
    
    public final Team addCreature(Creature creature)
    {
        if(!creatures.contains(creature))
            creatures.add(creature);
        return this;
    }
    
    public final Team removeCreature(Creature creature)
    {
        creatures.remove(creature);
        return this;
    }
    
    public final int size() { return creatures.size(); }
    
    public final Creature getCreature(int index) { return creatures.get(index); }
    
    public final boolean isValidIndex(int index) { return index > 0 && index < creatures.size(); }
    
    public final boolean hasAnyAlive()
    {
        return creatures.stream().anyMatch(Creature::isAlive);
    }
    
    public final int getAliveCount()
    {
        return creatures.stream().mapToInt(c -> c.isAlive() ? 1 : 0).sum();
    }
    
    public final void setSearchFirstBehabior(SearchFirstBehabior behabior) { this.searchFirst = behabior; }
    public final void setSearchNextBehabior(SearchNextBehabior behabior) { this.searchNext = behabior; }
    
    public final SearchFirstBehabior getSearchFirstBehabior() { return searchFirst; }
    public final SearchNextBehabior getSearchNextBehabior() { return searchNext; }
    
    public final void setIntelligence(AIIntelligence intel) { this.intel = intel == null ? AIIntelligence.createDummy() : intel; }
    public final AIIntelligence getIntelligence() { return intel == null ? AIIntelligence.createDummy() : intel; }
    
    
    public final Creature findFirst(RNG rng, SearchFirstBehabior behabior)
    {
        if(creatures.size() < 2)
            return creatures.get(0);
        switch(behabior)
        {
            case FIRST:
                for(Creature c : creatures)
                    if(c.isAlive())
                        return c;
            case RANDOM_WITHOUT_LAST:
                if(creatures.size() < 2)
                    return creatures.get(0);
                return creatures.get(rng.d(creatures.size() - 1));
            case RANDOM:
                return creatures.get(rng.d(creatures.size()));
        }
        return null;
    }
    public final Creature findFirst(RNG rng) { return findFirst(rng, searchFirst); }
    
    public final Creature findNext(Creature enemy, RNG rng, WeatherId weather)
    {
        if(!hasAnyAlive())
            return null;
        switch(searchNext)
        {
            case ORDERED:
                for(Creature c : creatures)
                    if(c.isAlive())
                        return c;
            case SEARCH_WITHOUT_LAST: {
                var opt = creatures.subList(0, creatures.size() - 1).stream()
                        .filter(Creature::isAlive)
                        .map(c -> new CreatureSelected(c, enemy, rng, weather))
                        .reduce(Team::selectCreature);
                return opt.isPresent() ? opt.get().creature : creatures.get(creatures.size() - 1);
            }
            case SEARCH:
                return creatures.stream()
                        .filter(Creature::isAlive)
                        .map(c -> new CreatureSelected(c, enemy, rng, weather))
                        .reduce(Team::selectCreature)
                        .get().creature;
        }
        return null;
    }
    
    public final CreatureClass getTeamClass()
    {
        return Formula.creaturesClass(creatures.toArray(Creature[]::new));
    }
    
    public final float getLevelAverage()
    {
        if(creatures.isEmpty())
            return 0;
        if(creatures.size() == 1)
            return creatures.get(0).getLevel();
        return creatures.stream().mapToInt(Creature::getLevel).sum() / (float) creatures.size();
    }
    
    
    public enum SearchFirstBehabior { FIRST, RANDOM_WITHOUT_LAST, RANDOM }
    public enum SearchNextBehabior { SEARCH, SEARCH_WITHOUT_LAST, ORDERED }
    
    private final class CreatureSelected
    {
        private final Creature creature;
        private final AIScore score;
        
        private CreatureSelected(Creature creature, Creature enemy, RNG rng, WeatherId weather)
        {
            this.creature = creature;
            FighterTurnState state = new FighterTurnState(creature, enemy, new BattleCommandManager(), rng, true, weather);
            this.score = creature.getAttackManager().selectScoreByAI(state, intel);
        }
    }
    
    private static CreatureSelected selectCreature(CreatureSelected t, CreatureSelected u)
    {
        return t.score.compareTo(u.score) >= 0 ? t : u;
    }
    
}
