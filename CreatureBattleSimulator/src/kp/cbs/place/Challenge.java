/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.place;

import java.awt.Window;
import kp.cbs.PlayerGame;
import kp.cbs.battle.Battle;
import kp.cbs.battle.BattleResult;
import kp.cbs.battle.Encounter;
import kp.cbs.battle.prop.BattleProperties;
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
    @Property private int battlesPerStage = 1;
    @Property private String battle = "";
    
    private BattleProperties cache;
    
    public final String getName() { return name; }
    public final String getDescription() { return description; }
    public final int getBattlesPerStage() { return battlesPerStage; }
    
    private Encounter generateEncounter()
    {
        if(cache == null)
        {
            cache = BattlePropertiesPool.load(battle);
            if(cache == null)
                throw new IllegalStateException("Battle " + battle + " not found");
        }
        return cache.createEncounter();
    }
    
    public final BattleResult startBattle(Window parent, PlayerGame game, Creature... selfCreatures)
    {
        if(selfCreatures == null || selfCreatures.length < 1)
                throw new IllegalStateException();
        
        var encounter = generateEncounter();
        encounter.setExperienceBonus(1.5f);
        for(var creature : selfCreatures)
            encounter.getSelfTeam().addCreature(creature);

        return Battle.initiate(parent, game, encounter);
    }
}
