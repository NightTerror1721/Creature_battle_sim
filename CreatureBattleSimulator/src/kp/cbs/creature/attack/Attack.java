/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Objects;
import kp.cbs.battle.FighterTurnState;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.attack.effects.AIIntelligence;
import kp.cbs.creature.attack.effects.AIScore;
import kp.cbs.creature.elements.ElementalType;
import kp.udl.autowired.AutowiredSerializer;
import kp.udl.data.UDLValue;

/**
 *
 * @author Asus
 */
public final class Attack
{
    private AttackModel model;
    private String description;
    private int currentPP;
    
    Attack(AttackModel model)
    {
        this.model = Objects.requireNonNull(model);
        this.currentPP = model.getMaxPP();
    }
    
    public final AttackModel getModel() { return model; }
    
    public final int getId() { return model.getId(); }
    
    public final String getName() { return model.getName(); }
    
    public final int getPower() { return model.getPower(); }
    
    public final int getMaxPP() { return model.getMaxPP(); }
    
    public final boolean hasPP() { return currentPP > 0; }
    public final int getCurrentPP() { return currentPP; }
    public final void usePP(int amount) { this.currentPP = Math.max(0, currentPP - Math.abs(amount)); }
    public final void restorePP(int amount) { this.currentPP = Math.min(model.getMaxPP(), currentPP + Math.abs(amount)); }
    public final void restoreAllPP() { this.currentPP = model.getMaxPP(); }
    
    public final boolean isInfallible() { return model.isInfallible(); }
    public final int getPrecision() { return model.getPrecision(); }
    
    public final int getPriority() { return model.getPriority(); }
    
    public final ElementalType getElementalType() { return model.getElementalType(); }
    
    public final int getTurnCount() { return model.getTurnCount(); }
    public final AttackTurn getTurn(int index) { return model.getTurn(index); }
    public final AttackTurn[] getAllTurns() { return model.getAllTurns(); }
    
    public final void apply(FighterTurnState state, int turnId)
    {
        AttackTurn turn = model.getTurn(turnId);
        if(turn != null)
            turn.apply(state);
    }
    
    public final AIScore computeAIScore(FighterTurnState state, AIIntelligence intel) { return model.computeAIScore(state, intel); }
    
    public final String getDescription()
    {
        return description == null ? description = model.generateDescription() : description;
    }
    
    
    public static final AutowiredSerializer<Attack> SERIALIZER = new AutowiredSerializer<Attack>(Attack.class)
    {
        @Override
        public final UDLValue serialize(Attack value)
        {
            return UDLValue.valueOf(value.model.getId());
        }
        
        @Override
        public final Attack unserialize(UDLValue value)
        {
            int modelId = value.getInt();
            return AttackPool.createAttack(modelId);
        }
    };
}
