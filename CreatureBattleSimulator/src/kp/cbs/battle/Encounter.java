/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.util.Objects;
import kp.cbs.battle.Team.SearchFirstBehabior;
import kp.cbs.battle.Team.SearchNextBehabior;
import kp.cbs.creature.attack.effects.AIIntelligence;

/**
 *
 * @author Asus
 */
public final class Encounter
{
    private final Team selfTeam = new Team();
    private final Team enemyTeam = new Team();
    private float expBonus = 1f;
    private String music = "trainer";
    
    
    public final Team getSelfTeam() { return selfTeam; }
    public final Team getEnemyTeam() { return enemyTeam; }
    
    public final void setSearchFirstBehabior(SearchFirstBehabior behabior) { enemyTeam.setSearchFirstBehabior(behabior); }
    public final void setSearchNextBehabior(SearchNextBehabior behabior) { enemyTeam.setSearchNextBehabior(behabior); }
    
    public final SearchFirstBehabior getSearchFirstBehabior() { return enemyTeam.getSearchFirstBehabior(); }
    public final SearchNextBehabior getSearchNextBehabior() { return enemyTeam.getSearchNextBehabior(); }
    
    public final void setIntelligence(AIIntelligence intel) { enemyTeam.setIntelligence(intel); }
    public final AIIntelligence getIntelligence() { return enemyTeam.getIntelligence(); }
    
    public final void setExperienceBonus(float bonus) { this.expBonus = Math.max(1f, bonus); }
    public final float getExperienceBonus() { return expBonus; }
    
    public final void setMusic(String music) { this.music = Objects.requireNonNull(music); }
    public final String getMusic() { return music; }
}
