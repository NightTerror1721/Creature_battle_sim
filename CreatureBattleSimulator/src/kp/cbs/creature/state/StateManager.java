/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.state;

import kp.cbs.battle.BattleUpdater;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.feat.PercentageFeature;

/**
 *
 * @author Asus
 */
public final class StateManager implements BattleUpdater
{
    private final PercentageFeature accuracy = new PercentageFeature();
    private final PercentageFeature evasion = new PercentageFeature();
    private TwoTurnsEffect criticalHitBonus = TwoTurnsEffect.OFF;
    private TwoTurnsEffect resting = TwoTurnsEffect.OFF;
    private boolean intimidated;
    
    public final PercentageFeature getAccuracy() { return accuracy; }
    public final PercentageFeature getEvasion() { return evasion; }
    
    public final void setCriticalHitBonus(boolean flag) { this.criticalHitBonus = effect(flag); }
    public final boolean isCriticalHitBonus() { return criticalHitBonus != TwoTurnsEffect.OFF; }
    
    public final void setResting(boolean flag) { this.resting = effect(flag); }
    public final boolean isResting() { return resting != TwoTurnsEffect.OFF; }
    
    public final void setIntimidated(boolean flag) { this.intimidated = flag; }
    public final boolean isIntimidated() { return intimidated; }
    
    public final void clearAllStates()
    {
        accuracy.clearAlterations();
        evasion.clearAlterations();
        criticalHitBonus = TwoTurnsEffect.OFF;
        resting = TwoTurnsEffect.OFF;
        intimidated = false;
    }
    
    @Override
    public final void battleUpdate(FighterTurnState state)
    {
        if(state.isTurnEnd())
        {
            if(criticalHitBonus == TwoTurnsEffect.PHASE_1)
                criticalHitBonus = TwoTurnsEffect.PHASE_2;
            else criticalHitBonus = TwoTurnsEffect.OFF;
            
            if(resting == TwoTurnsEffect.PHASE_1)
                resting = TwoTurnsEffect.PHASE_2;
            else resting = TwoTurnsEffect.OFF;
            
            intimidated = false;
        }
        else
        {
            if(criticalHitBonus == TwoTurnsEffect.PHASE_2)
                criticalHitBonus = TwoTurnsEffect.OFF;
            
            if(resting == TwoTurnsEffect.PHASE_2)
                resting = TwoTurnsEffect.OFF;
        }
    }
    
    
    private static TwoTurnsEffect effect(boolean flag) { return flag ? TwoTurnsEffect.PHASE_1 : TwoTurnsEffect.OFF; }
    private enum TwoTurnsEffect { PHASE_1, PHASE_2, OFF; }
}
